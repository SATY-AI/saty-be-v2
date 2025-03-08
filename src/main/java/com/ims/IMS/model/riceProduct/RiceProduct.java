package com.ims.IMS.model.riceProduct;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rice_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiceProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String uniqueCode;
    private String name;
    private String description;
    private String manufacturer;
    private String advantage;
    private String disadvantage;
    private String origin;  // Xuất xứ của gạo (VD: Việt Nam, Thái Lan, Campuchia)
    private String riceType;  // Loại gạo (VD: Gạo tím, Gạo lứt, Gạo nếp, Gạo thơm...)
    private String nutritionFacts;  // Dinh dưỡng (Anthocyanin, chất xơ, vitamin, khoáng chất)
    private String healthBenefits;  // Lợi ích sức khỏe (Tốt cho tim mạch, hỗ trợ tiêu hóa...)
    private String cookingInstructions;  // Cách nấu gạo
    private String storageInstructions;  // Cách bảo quản
    private String foodPairings;  // Gợi ý kết hợp món ăn (VD: Cơm gạo tím, sushi, cháo...)
    private String imageUrl;  // URL ảnh minh họa sản phẩm
}