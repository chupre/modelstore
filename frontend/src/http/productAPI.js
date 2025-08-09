import {$authHost, $host} from "@/http/index.js";

export const fetchProducts = async ({
                                        page = 0,
                                        size = 20,
                                        category,
                                        search,
                                        sortBy,
                                        priceRange
                                    }
) => {
    return $host.get(`/products`, {params: {
        page,
        size,
        categoryId: category || undefined,
        search: search || undefined,
        sort: sortBy || "id",
        minPrice: priceRange ? priceRange[0] : undefined,
        maxPrice: priceRange ? priceRange[1] : undefined,
    }});
}

export const fetchProduct = async (id) => {
    try {
        return await $host.get(`/products/${id}`, {})
    } catch (error) {
        if (error.response && error.response.status === 404) {
            return null;
        }
    }
}

export const fetchCategories = async (page = 0, size = 20, sort = "name") => {
    return await $host.get(`/categories`, {params: {page, size, sort}});
}

export const createProduct = async (productFormData) => {
    return await $authHost.post(`/products`, productFormData, {
        headers: {
            "content-type": "multipart/form-data"
        }
    })
}

export const deleteProduct = async (id) => {
    return await $authHost.delete(`/products/${id}`, {})
}