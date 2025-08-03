import { $authHost, $host } from "./index";

export const registration = async (username, email, password) => {
    const response = await $host.post('/users')
    return response
}

export const login = async (email, password) => {
    const response = await $host.post('/auth/login')
    return response
}

export const check = async () => {
    const response = await $host.post('/users')
    return response
}