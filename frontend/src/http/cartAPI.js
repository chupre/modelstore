import {$authHost} from "@/http/index.js";

export const createCart = async (userId) => {
    return await $authHost.post('/carts', {userId: userId})
}

export const fetchCart = async (userId, createIfNotExist) => {
    try {
        return await $authHost.get(`/carts/users/${userId}`)
    } catch (e) {
        if (e.response && e.response.status === 404 && createIfNotExist) {
            return await createCart(userId)
        }
    }
}

export const clearCart = async (id) => {
    await $authHost.delete(`/carts/${id}/items`)
}

export const addCartItem = async (cartId, productId) => {
    return await $authHost.post(`/carts/${cartId}/items`, {productId: productId})
}

export const deleteCartItem = async (cartId, productId) => {
    await $authHost.delete(`/carts/${cartId}/items/${productId}`)
}

export const toggleSelectItem = async (cartId, productId) => {
    await $authHost.post(`/carts/${cartId}/items/${productId}/toggleSelect`)
}

export const selectAllItems = async (cartId) => {
    await $authHost.post(`/carts/${cartId}/items/selectAll`)
}

export const unselectAllItems = async (cartId) => {
    await $authHost.post(`/carts/${cartId}/items/unselectAll`)
}

export const checkout = async (cartId, currency, redirectUrl) => {
    return await $authHost.post(`/checkout`, {cartId, currency, redirectUrl})
}
