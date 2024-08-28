package com.sagamal.shoppingcart.service.image;

import com.sagamal.shoppingcart.dto.ImageDto;
import com.sagamal.shoppingcart.exceptions.ResourceNotFoundException;
import com.sagamal.shoppingcart.model.Image;
import com.sagamal.shoppingcart.model.Product;
import com.sagamal.shoppingcart.repository.image.ImageRepository;
import com.sagamal.shoppingcart.repository.product.ProductRepository;
import com.sagamal.shoppingcart.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    private final ProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Image not found")
        );
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, ()->{
            throw new ResourceNotFoundException("Image not found");
        });
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImagesDto = new ArrayList<>();
        for(MultipartFile file: files){
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);
                //create the url to return the image for download
                String baseDownloadUrl = "${api.prefix}/images/download/";
                String generatedDownloadUrl = baseDownloadUrl+ image.getId();
                image.setDownloadUrl(generatedDownloadUrl);
                Image savedImage = imageRepository.save(image);
                //set the real saved image id in the download url and save the modification
                savedImage.setDownloadUrl(baseDownloadUrl+savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDowndoadUrl(savedImage.getDownloadUrl());

                savedImagesDto.add(imageDto);

            }catch (IOException | SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }

        return savedImagesDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = this.getImageById(imageId);
        try{
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        }catch (IOException| SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
