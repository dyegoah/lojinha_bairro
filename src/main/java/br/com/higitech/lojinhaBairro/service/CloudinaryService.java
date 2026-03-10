package br.com.higitech.lojinhaBairro.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImagem(MultipartFile arquivo) throws IOException {
        // Envia o arquivo para o Cloudinary e pega a resposta
        Map<?, ?> uploadResult = cloudinary.uploader().upload(arquivo.getBytes(), ObjectUtils.emptyMap());
        
        // Retorna a URL gerada pela nuvem
        return uploadResult.get("url").toString();
    }
}