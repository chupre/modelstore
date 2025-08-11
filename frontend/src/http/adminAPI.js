import {$authHost} from "@/http/index.js";

export const createProduct = async (productFormData) => {
    return await $authHost.post(`admin/products`, productFormData, {
        headers: {
            "content-type": "multipart/form-data"
        }
    })
}
export const patchProduct = async (id, productFormData) => {
    return await $authHost.patch(`admin/products/${id}`, productFormData, {
        headers: {
            "content-type": "multipart/form-data"
        }
    })
}
export const deleteProduct = async (id) => {
    return await $authHost.delete(`admin/products/${id}`, {})
}
export const createCategory = async (category) => {
    return await $authHost.post(`admin/categories`, category)
}
export const deleteCategory = async (id) => {
    return await $authHost.delete(`admin/categories/${id}`, {})
}
export const updateCategory = async (id, category) => {
    return await $authHost.put(`admin/categories/${id}`, category)
}


export const fetchUsers = async (page, size) => {
    return await $authHost.get('admin/users', {params: {
            page,
            size
        }})
}

export const updateUser = async (id, user) => {
    return await $authHost.put(`admin/users/${id}`, user)
}

export const deleteUser = async (id) => {
    return await $authHost.delete(`admin/users/${id}`, {})
}

export const fetchCarts = async (page, size) => {
    return await $authHost.get(`admin/carts`, {params: {
            page,
            size
        }})
}