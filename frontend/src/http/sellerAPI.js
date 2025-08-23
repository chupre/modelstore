import {$authHost} from "@/http/index.js";

export const becomeSeller = async (userId, payoutMethod, payoutDestination) => {
    await $authHost.post(`/sellers`, {userId, payoutMethod, payoutDestination})
}

export const fetchCurrentSeller = async () => {
    return await $authHost.get(`/sellers/me`)
}

export const fetchSellerProducts = async (id, page, size, sort="id") => {
    return await $authHost.get(`/sellers/${id}/products`, {params: {
        page,
        size,
        sort
    }})
}

export const createProduct = async (productFormData) => {
    return await $authHost.post(`/sellers/me/products`, productFormData, {
        headers: {
            "content-type": "multipart/form-data"
        }
    })
}

export const patchProduct = async (id, productFormData) => {
    return await $authHost.patch(`/sellers/me/products/${id}`, productFormData, {
        headers: {
            "content-type": "multipart/form-data"
        }
    })
}

export const deleteProduct = async (id) => {
    return await $authHost.delete(`sellers/me/products/${id}`, {})
}

export const updateSeller = async (payoutMethod, payoutDestination) => {
    return await $authHost.put(`/sellers/me`, {payoutMethod, payoutDestination})
}

export const fetchSellerPayouts = async () => {
    return await $authHost.get(`/sellers/me/payouts`)
}
