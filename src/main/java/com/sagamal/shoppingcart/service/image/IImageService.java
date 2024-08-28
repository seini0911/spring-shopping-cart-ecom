package com.sagamal.shoppingcart.service.image;

import com.sagamal.shoppingcart.dto.ImageDto;
import com.sagamal.shoppingcart.model.Image;
import com.sagamal.shoppingcart.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImage(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long imageId);
}
