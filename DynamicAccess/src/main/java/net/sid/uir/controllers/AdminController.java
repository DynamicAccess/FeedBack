package net.sid.uir.controllers;

import java.awt.PageAttributes.MediaType;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.sid.uir.entities.Categorie;
import net.sid.uir.metier.IAdmin;

@Controller
@RequestMapping(value="/admin")
public class AdminController {
	@Autowired
   private IAdmin metier;
@RequestMapping(value="/index")
	public String index(Model model){
	model.addAttribute("categorie",new Categorie());
	model.addAttribute("categories",metier.listCategories());
	return "categories";
// return "Admin";
}

@RequestMapping(value="/saveCat")
	public String saveCAt(Categorie c,BindingResult bindingResult, Model model,MultipartFile file ) throws IOException{
		if(bindingResult.hasErrors()) {
			return("categories");	
	}
		if(!file.isEmpty()){
			BufferedImage bi=ImageIO.read(file.getInputStream());
			c.setPhoto(file.getBytes());
            c.setNomPhoto(file.getOriginalFilename());
		}
		metier.ajouterCategorie(c);
		model.addAttribute("categorie",new Categorie());
		model.addAttribute("categories",metier.listCategories());
		return "categories";
	}
@RequestMapping(value="/photoCat",produces=MediaType.IMAGE_JPEG_VALUE)
@ResponseBody
public byte[] getPhoto(Long idCat) throws IOException{ Categorie c=metier.getCategorie(idCat); if(c.getPhoto()==null) return new byte[0];
else return IOUtils.toByteArray(new ByteArrayInputStream(c.getPhoto()));
}

	
}
