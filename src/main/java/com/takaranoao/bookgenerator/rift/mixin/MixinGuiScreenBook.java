package com.takaranoao.bookgenerator.rift.mixin;

import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenBook.class)
public class MixinGuiScreenBook {
	@Final
	@Shadow
	private EntityPlayer editingPlayer;
	@Shadow
	private NBTTagList bookPages;
	@Shadow
	private int bookTotalPages;
	@Shadow
	private boolean bookIsModified;
	@Inject(method = "sendBookToServer",at = @At("HEAD"))
	private void onSendBookToServer(boolean publish, CallbackInfo ci){
		if(bookPages != null && bookPages.size()>0){
			if(bookPages.getString(0).equals("AutoGen")){
				while (bookPages.size() < 50){
					this.bookPages.add(new NBTTagString(""));
					++this.bookTotalPages;
					this.bookIsModified = true;
				}
				for (int i=0;i<bookPages.size();i++){
					StringBuilder t1 = new StringBuilder();
					while (t1.length()<255){
						t1.append((char) ((int) (Math.random() * 255) + 1));
					}
					bookPages.set(i, new NBTTagString(t1.toString()));
				}
				if(editingPlayer != null){
					editingPlayer.sendMessage(new TextComponentString(
							I18n.format("bookgenerator.notify.autogen")
					));
				}

			}
		}
	}
}
