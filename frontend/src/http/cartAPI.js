import {$authHost} from "@/http/index.js";

export const createCart = async (userId) => {
    return await $authHost.post('/carts', {userId: userId})
}

export const fetchCart = async (userId) => {
    try {
        return await $authHost.get(`/carts/users/${userId}`)
    } catch (e) {
        if (e.response && e.response.status === 404) {
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

export const fetchCarts = async () => {
    return await $authHost.get(`carts`, {})
}

