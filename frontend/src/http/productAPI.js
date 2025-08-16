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

export const fetchCategories = async (page, size, sort = "name") => {
    return await $host.get(`/categories`, {params: {
        page,
        size,
        sort
    }});
}

export const downloadProductModel = async (id) => {
    return await $authHost.get(`products/${id}`, {responseType: "blob"})
}

export const fetchLikedProducts = async (id) => {
    return await $authHost.get(`/users/${id}/likes`)
}

export const likeProduct = async (id) => {
    return await $authHost.post(`/interactions/products/${id}/likes`)
}

export const unlikeProduct = async (id) => {
    return await $authHost.delete(`/interactions/products/${id}/likes`)
}

export const fetchComments = async(id) => {
    return await $host.get(`/interactions/products/${id}/comments?sort=createdAt,desc`)
}

export const comment = async (id, comment) => {
    return await $authHost.post(`/interactions/products/${id}/comments`, {comment})
}

export const fetchLikedComments = async (userId, productId) => {
    return $authHost.get(`/users/${userId}/products/${productId}/commentLikes`)
}

export const likeComment = async (id) => {
    return $authHost.post(`interactions/products/comments/${id}/likes`)
}

export const unlikeComment = async (id) => {
    return $authHost.delete(`interactions/products/comments/${id}/likes`)
}