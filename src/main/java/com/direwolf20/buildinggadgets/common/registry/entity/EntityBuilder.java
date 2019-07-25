package com.direwolf20.buildinggadgets.common.registry.entity;

import com.direwolf20.buildinggadgets.common.registry.RegistryObjectBuilder;
import com.google.common.base.Preconditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.Builder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class EntityBuilder<T extends Entity> extends RegistryObjectBuilder<EntityType<?>, Builder<T>> {
    private Class<T> entityClass;
    private Supplier<Supplier<IRenderFactory<? super T>>> renderFactory;
    public EntityBuilder(String registryName) {
        super(registryName);
    }

    public EntityBuilder(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public EntityBuilder<T> factory(Function<Builder<T>, EntityType<?>> factory) {
        return (EntityBuilder<T>) super.factory(factory);
    }

    @Override
    public EntityBuilder<T> builder(Builder<T> builder) {
        return (EntityBuilder<T>) super.builder(builder);
    }

    public EntityBuilder<T> renderer(Class<T> entityClass, Supplier<Supplier<IRenderFactory<? super T>>> renderFactory) {
        this.entityClass = Objects.requireNonNull(entityClass);
        this.renderFactory = Objects.requireNonNull(renderFactory);
        return this;
    }

    @Override
    public EntityType<?> construct() {
        Preconditions.checkState(renderFactory!=null,"Cannot construct an Entity without an Renderer!");
        Preconditions.checkState(entityClass != null, "Cannot construct an Entity of unknown class!");
        return super.construct();
    }

    private Class<T> getEntityClass() {
        Preconditions.checkState(entityClass!=null,"Cannot request Entity class before type has been constructed!");
        return entityClass;
    }

    private IRenderFactory<? super T> getRenderFactory() {
        Preconditions.checkState(renderFactory != null && renderFactory.get() != null && renderFactory.get().get() != null, "Expected Renderer to be present before EntityType has been constructed!");
        return renderFactory.get().get();
    }

    void registerRenderer() {
        RenderingRegistry.registerEntityRenderingHandler(getEntityClass(), getRenderFactory());
    }
}