package com.github.rakstern.entity.custom;

import com.github.rakstern.RainStalker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;

public class RainStalkerEntity extends HostileEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final TrackedData<Boolean> FISHED = DataTracker.registerData(RainStalkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    // When looked at with core intact, freeze in place.
    public static final TrackedData<Boolean> RS_OBSERVED = DataTracker.registerData(RainStalkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    //RainStalker has opacity needs, it becomes more transparent depending on observation and weather
    public static final TrackedData<Float> OPACITY = DataTracker.registerData(RainStalkerEntity.class, TrackedDataHandlerRegistry.FLOAT);

    private static final Identifier SPEED_BOOST_ID = Identifier.of(RainStalker.MOD_ID, "fished_speed_boost");
    private static final EntityAttributeModifier FISHED_SPEED_MODIFIER = new EntityAttributeModifier(
            SPEED_BOOST_ID, 0.1, EntityAttributeModifier.Operation.ADD_VALUE); // +10% speed

    public RainStalkerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        //TO-DO: Fix the RainStalker's AI when in water. Medium priority.
    }

    public static DefaultAttributeContainer.Builder createRainStalkerAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0D) // A bit tankier than an Enderman
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        // When fished, it's hostile time
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.5D, false) {
            @Override
            public boolean canStart() {
                return isFished() && super.canStart();
            }
        });

        //Otherwise it's stalking time... but will it even attack when stalking? TO-DO: Make sure it attacks
        this.goalSelector.add(2, new StalkerSensingGoal(this));
        this.goalSelector.add(3, new FlankPlayerGoal(this));

        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));

        // Targets: Attack players and Iron Golems
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 0, event -> {
            // Set speed based on fished state
            // If fished, we go 2x speed for a "panic" effect
            event.getController().setAnimationSpeed(this.isRSObserved() ? 0.0 : (this.isFished() ? 2.0 : 1.0));

            if (event.isMoving()) {
                String walkAnim = this.isFished() ? "animation.rainstalker.walk_fished" : "animation.rainstalker.walk";
                return event.setAndContinue(RawAnimation.begin().thenLoop(walkAnim));
            }

            // Pick the idle based on whether it's hollow or not
            String idleAnim = this.isFished() ? "animation.rainstalker.idle_fished" : "animation.rainstalker.idle";
            return event.setAndContinue(RawAnimation.begin().thenLoop(idleAnim));
        }));

        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "twitch_controller", 0, event -> {
            // Twitching also gets faster and more frantic when hollow
            event.getController().setAnimationSpeed(this.isRSObserved() ? 0.0 : (this.isFished() ? 1.5 : 1.0));

            if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                int twitchChance = this.getWorld().isThundering() ? 50 : 200;

                if (this.getRandom().nextInt(twitchChance) == 0) {
                    event.getController().forceAnimationReset();
                    return event.setAndContinue(RawAnimation.begin().thenPlay("animation.rainstalker.twitch"));
                }
                if (this.getRandom().nextInt(300) == 0) {
                    event.getController().forceAnimationReset();
                    return event.setAndContinue(RawAnimation.begin().thenPlay("animation.rainstalker.look_left"));
                }
            }
            return PlayState.CONTINUE;
        }));

        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "death_controller", 0, event -> {
            if (this.isDead()) {
                return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.rainstalker.death"));
            }
            return PlayState.STOP;
        }));
    }

    @Override
    protected boolean isAffectedByDaylight() {
        return false; //Just incase this would somehow have been true
    }

    @Override
    public void tick() {
        super.tick();

        //Run this part on the server
        if(!this.getWorld().isClient){
            float targetOpacity = this.getWorld().isRaining() ? 0.8f : 0.15f;

            // If it's being looked at (seen), it starts to "dissolve"
            if (this.isRSObserved()) {
                targetOpacity -= 0.6f; // Become much more transparent
            }

            // Clamp values between 0.05 (barely visible) and 1.0 (fully visible)
            targetOpacity = Math.max(0.05f, Math.min(1.0f, targetOpacity));

            // Smooth transition (Interpolation)
            float currentOpacity = this.getOpacity();
            float lerpSpeed = 0.05f;
            this.setOpacity(currentOpacity + (targetOpacity - currentOpacity) * lerpSpeed);

            // If it becomes nearly invisible while looked at, break the freeze!
            if (this.isRSObserved() && this.getOpacity() < 0.3f) {
                this.setRSObserved(false);
                // Optional: Teleport it slightly away to simulate it "vanishing"
            }
        }

        // Only run this part on the client (visuals)
        if (this.getWorld().isClient()) {
            if(this.getOpacity() > 0.4f){ //Only show particles as long as it's visible enough
                // If it's raining or the mob is wet, make it drip more!
                boolean isWet = this.getWorld().isRaining() || this.isInsideWaterOrBubbleColumn();
                int particleCount = isWet ? 3 : 1;

                for (int i = 0; i < particleCount; i++) {
                    if (this.random.nextFloat() < 0.15f) { // 15% chance per tick to drip
                        double x = this.getX() + (this.random.nextDouble() - 0.5) * this.getWidth();
                        double y = this.getY() + this.random.nextDouble() * this.getHeight();
                        double z = this.getZ() + (this.random.nextDouble() - 0.5) * this.getWidth();

                        // DRIPPING_WATER looks great, but FALLING_WATER is faster
                        this.getWorld().addParticle(ParticleTypes.FALLING_DRIPSTONE_WATER, x, y, z, 0, 0, 0);
                    }
                }

                // Add "Splash" particles at the feet if it's moving in the rain
                if (isWet && this.isMoving()) {
                    this.getWorld().addParticle(ParticleTypes.RAIN,
                            this.getX() + (this.random.nextDouble() - 0.5) * this.getWidth(),
                            this.getY(),
                            this.getZ() + (this.random.nextDouble() - 0.5) * this.getWidth(),
                            0, 0, 0);
                }
            }
        }
    }

    // Helper method to check if the mob is walking
    private boolean isMoving() {
        return this.getVelocity().horizontalLengthSquared() > 1.0E-6D;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);

        if (!this.getWorld().isClient) return;

        // The 'Dissolve' Effect
        int particleCount = this.isFished() ? 100 : 50; // More 'evaporation' if it's hollow
        ParticleEffect particleType = this.isFished() ? ParticleTypes.CLOUD : ParticleTypes.SPLASH;

        for (int i = 0; i < particleCount; i++) {
            double x = this.getX() + (this.random.nextDouble() - 0.5) * this.getWidth();
            double y = this.getY() + this.random.nextDouble() * this.getHeight();
            double z = this.getZ() + (this.random.nextDouble() - 0.5) * this.getWidth();

            // Spread them out more if it's the 'hollow' death
            double speedX = (this.random.nextDouble() - 0.5) * 0.5;
            double speedY = this.random.nextDouble() * 0.5;
            double speedZ = (this.random.nextDouble() - 0.5) * 0.5;

            this.getWorld().addParticle(particleType, x, y, z, speedX, speedY, speedZ);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        MobNavigation navigation = new MobNavigation(this, world);
        // This allows the mob to actually enter and move through water in its pathing
        navigation.setCanSwim(true);
        return navigation;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FISHED, false); // Default to not fished
        builder.add(RS_OBSERVED, false); // TO-DO: Rename to specifically RSOBserved to avoid possible conflict of naming?
        builder.add(OPACITY, 1.0f); //Default value is to be as visible as the texture allows
    }

    // Getters and Setters
    public boolean isFished() {
        return this.dataTracker.get(FISHED);
    }

    public void setFished(boolean fished) {
        // Just update the data, don't play sounds here!
        this.dataTracker.set(FISHED, fished);

        if (fished) {
            // Apply attributes (Safe to call multiple times)
            EntityAttributeInstance speed = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            if (speed != null && !speed.hasModifier(SPEED_BOOST_ID)) {
                speed.addTemporaryModifier(FISHED_SPEED_MODIFIER);
            }
        }
    }

    // This avoids sounds being played whenever we need to setFished on world load
    public void onFished() {
        if (!this.isFished()) {
            this.setFished(true);
            this.setRSObserved(false);
            this.getWorld().playSound(null, this.getBlockPos(),
                    SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.HOSTILE, 1.6f, 0.5f); //TO-DO: Replace with custom sound

            // Target the nearest player immediately
            this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        }
    }


    public boolean isRSObserved() { return this.dataTracker.get(RS_OBSERVED); }
    public void setRSObserved(boolean observed) { this.dataTracker.set(RS_OBSERVED, observed); }

    public float getOpacity() { return this.dataTracker.get(OPACITY); }
    public void setOpacity(float opacity) { this.dataTracker.set(OPACITY, opacity); }

    @Override
    protected SoundEvent getAmbientSound() {
        // With core: soft dripping/burbling. Without core: hollow wind.
        return this.isFished() ?
                SoundEvents.ENTITY_PHANTOM_AMBIENT : // Spooky wind-like sound
                SoundEvents.BLOCK_WATER_AMBIENT;    // Calm water noises
        //TO-DO: Add custom sounds eventually
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        // Higher pitch or more "hissing" when hollow
        return this.isFished() ?
                SoundEvents.ENTITY_VEX_HURT :
                SoundEvents.ENTITY_GENERIC_SPLASH;
        //TO-DO: Add custom sounds here too
    }

    @Override
    protected float getSoundVolume() {
        // Make the wind sounds slightly louder and more omnipresent
        return this.isFished() ? 1.5f : 0.7f;
    }

    @Override
    public float getSoundPitch() {
        // Drop the pitch for the wind to make it sound "deeper" and more ominous
        return this.isFished() ? 0.5f : 1.0f;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsFished", this.isFished());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setFished(nbt.getBoolean("IsFished"));
    }

    public static boolean canSpawn(EntityType<RainStalkerEntity> rainStalkerEntityEntityType, ServerWorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        // Spawn only in the rain
        if (!serverWorldAccess.toServerWorld().isRaining()) {
            return false;
        }

        // Check for other RainStalkers in a huge radius (128 blocks)
        // We use a Box to search the area around the potential spawn point.
        Box checkRadius = new Box(blockPos).expand(128);
        List<RainStalkerEntity> others = serverWorldAccess.getEntitiesByClass(RainStalkerEntity.class, checkRadius, entity -> true);

        return others.isEmpty(); // Only spawn if no others are found
    }


    //Using an inner class
    private class StalkerSensingGoal extends Goal{
        private final RainStalkerEntity stalkerEntity;
        private PlayerEntity targetPlayer;

        public StalkerSensingGoal(RainStalkerEntity stalkerEntity){
            this.stalkerEntity = stalkerEntity;
        }

        @Override
        public boolean canStart() {
            //Stalk whilst not fished
            if (this.stalkerEntity.isFished()) return false;

            this.targetPlayer = this.stalkerEntity.getWorld().getClosestPlayer(this.stalkerEntity, 24.0D);
            return this.targetPlayer != null;
        }

        @Override
        public void tick() {
            if (this.targetPlayer == null) return;

            if (isPlayerLooking(this.targetPlayer)) {
                this.stalkerEntity.setRSObserved(true);
                this.stalkerEntity.getNavigation().stop();
            } else {
                this.stalkerEntity.setRSObserved(false);
                // Move toward player, but stop if within 2 blocks
                if (this.stalkerEntity.squaredDistanceTo(this.targetPlayer) > 4.0D) {
                    this.stalkerEntity.getNavigation().startMovingTo(this.targetPlayer, 1.1D);
                }
            }
        }

        private boolean isPlayerLooking(PlayerEntity player) {
            // Math: Is the player's look vector pointing toward the stalker?
            Vec3d lookVec = player.getRotationVec(1.0F).normalize();
            Vec3d relativeVec = new Vec3d(stalkerEntity.getX() - player.getX(), stalkerEntity.getEyeY() - player.getEyeY(), stalkerEntity.getZ() - player.getZ()).normalize();
            double dot = lookVec.dotProduct(relativeVec);

            // 0.5 is roughly a 60-degree field of view. 1.0 is a direct stare.
            return dot > 0.8 && player.canSee(stalkerEntity);
        }
    }

    //TO-DO: Flanking may need refining.
    private class FlankPlayerGoal extends Goal {
        private final RainStalkerEntity stalkerEntity;
        private PlayerEntity target;

        public FlankPlayerGoal(RainStalkerEntity stalkerEntity) {
            this.stalkerEntity = stalkerEntity;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            this.target = this.stalkerEntity.getWorld().getClosestPlayer(this.stalkerEntity, 20.0D);
            return this.target != null && !this.stalkerEntity.isFished() && !this.stalkerEntity.isRSObserved();
        }

        @Override
        public void tick() {
            if (target == null) return;

            // 1. Get the vector representing where the player is looking
            Vec3d lookDir = target.getRotationVec(1.0f).normalize();

            // 2. Calculate two potential points: 90 degrees Left and 90 degrees Right
            Vec3d leftFlank = target.getPos().add(lookDir.rotateY((float) Math.toRadians(110)).multiply(8.0));
            Vec3d rightFlank = target.getPos().add(lookDir.rotateY((float) Math.toRadians(-110)).multiply(8.0));

            // 3. Compare which point is closer to the RainStalker's CURRENT position
            // This ensures it doesn't cross the player's face to get to the "other" side
            double distToLeft = stalkerEntity.squaredDistanceTo(leftFlank);
            double distToRight = stalkerEntity.squaredDistanceTo(rightFlank);

            Vec3d bestDestination = (distToLeft < distToRight) ? leftFlank : rightFlank;

            // 4. Move to the best spot
            this.stalkerEntity.getNavigation().startMovingTo(bestDestination.x, bestDestination.y, bestDestination.z, 1.1D);

            // Always keep eyes on the prize
            this.stalkerEntity.getLookControl().lookAt(target, 30.0F, 30.0F);
        }
    }
}


