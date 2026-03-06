package com.github.rakstern.entity.custom;

import com.github.rakstern.RainStalker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RainStalkerEntity extends HostileEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final TrackedData<Boolean> FISHED = DataTracker.registerData(RainStalkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    // When looked at with core intact, freeze.
    public static final TrackedData<Boolean> FROZEN = DataTracker.registerData(RainStalkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final Identifier SPEED_BOOST_ID = Identifier.of(RainStalker.MOD_ID, "fished_speed_boost");
    private static final EntityAttributeModifier FISHED_SPEED_MODIFIER = new EntityAttributeModifier(
            SPEED_BOOST_ID, 0.1, EntityAttributeModifier.Operation.ADD_VALUE); // +10% speed

    public RainStalkerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
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
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));

        // Targets: Attack players and Iron Golems
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 0, event -> {
            // Set speed based on fished state
            // If fished, we go 2x speed for a "panic" effect
            event.getController().setAnimationSpeed(this.isFished() ? 2.0 : 1.0);

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
            event.getController().setAnimationSpeed(this.isFished() ? 1.5 : 1.0);

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
    public void tick() {
        super.tick();

        // Only run this on the client (visuals)
        if (this.getWorld().isClient()) {
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
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FISHED, false); // Default to not fished
    }

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

    // Create a NEW method for the actual fishing event
    public void onFished() {
        if (!this.isFished()) {
            this.setFished(true);
            this.getWorld().playSound(null, this.getBlockPos(),
                    SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.HOSTILE, 2.0f, 0.5f);

            // Target the nearest player immediately
            this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        }
    }

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
}
