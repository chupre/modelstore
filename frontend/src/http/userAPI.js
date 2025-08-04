import {$host} from "./index";
import {jwtDecode} from 'jwt-decode';

export const registration = async (username, email, password) => {
    await $host.post('/users', {username, email, password})
    const {data} = await $host.post('/auth/login', {email, password})
    localStorage.setItem('token', data.token)
    return jwtDecode(data.token)
}

export const login = async (email, password) => {
    const {data} = await $host.post('/auth/login', {email, password})
    localStorage.setItem('token', data.token)
    return jwtDecode(data.token)
}

export const refresh = async () => {
    const {data} = await $host.post('/auth/refresh')
    localStorage.setItem('token', data.token)
    return jwtDecode(data.token)
}