package com.ims.IMS.api.qrProductInfo.Admin;

public record ProductInfoUpdateRequest(
        String uniqueCodeWareHouse,
        String uniqueCodeProduct,
        //Product
        String name,
        String description,
        String manufacturer,
        String advantage,
        String disadvantage,
        String origin,  // Xuất xứ của gạo (VD: Việt Nam, Thái Lan, Campuchia)
        String riceType,  // Loại gạo (VD: Gạo tím, Gạo lứt, Gạo nếp, Gạo thơm...)
        String nutritionFacts,  // Dinh dưỡng (Anthocyanin, chất xơ, vitamin, khoáng chất)
        String healthBenefits,  // Lợi ích sức khỏe (Tốt cho tim mạch, hỗ trợ tiêu hóa...)
        String cookingInstructions,  // Cách nấu gạo
        String storageInstructions,  // Cách bảo quản
        String foodPairings,  // Gợi ý kết hợp món ăn (VD: Cơm gạo tím, sushi, cháo...)
        String imageUrl,  // URL ảnh minh họa sản phẩm
        //Warehouse
        String nameWareHouse,
        double pricePerKg,
        int stockQuantity,
        String informationWareHouse,
        String address,
        String phone
) {
}
