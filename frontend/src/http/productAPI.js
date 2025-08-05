import {$host} from "@/http/index.js";

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
        sort: sortBy || undefined,
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
    return $host.get(`/categories`, {params: {page, size, sort}});
}