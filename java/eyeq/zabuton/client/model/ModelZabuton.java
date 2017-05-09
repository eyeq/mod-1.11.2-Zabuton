package eyeq.zabuton.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelZabuton extends ModelBase {
    public ModelRenderer body;

    public ModelZabuton() {
        body = new ModelRenderer(this, 0, 0);
        body.addBox(-6.0F, -3.0F, -6.0F, 12, 3, 12);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        body.render(scale);
    }
}
