package net.arathain.malazan.common.render;

import net.arathain.malazan.common.entity.FlareEntity;
import net.arathain.malazan.common.entity.PortalEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PortalRenderer extends EntityRenderer<PortalEntity> {
    public PortalRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }


    @Override
    public void render(PortalEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(PortalEntity entity) {
        return null;
    }
}
