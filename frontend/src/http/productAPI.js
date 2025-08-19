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

export const fetchProductsWithUserLike = async ({
                                        page = 0,
                                        size = 20,
                                        category,
                                        search,
                                        sortBy,
                                        priceRange
                                    }
) => {
    return $authHost.get(`/products`, {params: {
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

export const fetchProductWithUserLike = async (id) => {
    try {
        return await $authHost.get(`/products/${id}`, {})
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

export const fetchLikedProducts = async (id, page, size, sort = "id") => {
    return await $host.get(`/users/${id}/products/likes`, {params: {
        page,
        size,
        sort
    }})
}

export const likeProduct = async (id) => {
    return await $authHost.post(`/interactions/products/${id}/likes`)
}

export const unlikeProduct = async (id) => {
    return await $authHost.delete(`/interactions/products/${id}/likes`)
}

export const fetchComments = async(id, page, size, sort = "createdAt,desc") => {
    return await $host.get(`/interactions/products/${id}/comments`, {params: {
            page,
            size,
            sort
        }})
}

export const fetchCommentsWithUserLike = async(id, page, size, sort = "createdAt,desc") => {
    return await $authHost.get(`/interactions/products/${id}/comments`, {params: {
            page,
            size,
            sort
        }})
}

export const comment = async (id, comment) => {
    return await $authHost.post(`/interactions/products/${id}/comments`, {comment})
}

export const fetchLikedComments = async (userId, page, size, sort = "id") => {
    return $host.get(`/users/${userId}/comments/likes`, {params: {
            page,
            size,
            sort
        }})
}

export const fetchUserComments = async (userId, page, size, sort = "id") => {
    return $host.get(`/users/${userId}/comments`, {params: {
            page,
            size,
            sort
        }})
}

export const likeComment = async (id) => {
    return $authHost.post(`interactions/products/comments/${id}/likes`)
}

export const unlikeComment = async (id) => {
    return $authHost.delete(`interactions/products/comments/${id}/likes`)
}

export const editComment = async (id, comment) => {
    return $authHost.put(`interactions/products/comments/${id}`, {comment})
}

export const deleteComment = async (id) => {
    return $authHost.delete(`interactions/products/comments/${id}`)
}