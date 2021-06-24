package net.arathain.malazan.common.render;


import net.arathain.malazan.common.entity.FlareEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
    public class FlareRenderer extends EntityRenderer<FlareEntity> {
        public FlareRenderer(EntityRendererFactory.Context ctx) {
            super(ctx);
        }

        @Override
        public boolean shouldRender(FlareEntity entity, Frustum frustum, double x, double y, double z) {
            return false;
        }

        @Override
        public Identifier getTexture(FlareEntity entity) {
            return null;
        }
    }

