import {$host} from "./index";
import {jwtDecode} from 'jwt-decode';

export const registration = async (username, email, password) => {
    const {data} = await $host.post('/users', {username, email, password})
    return jwtDecode(data.token);
}

export const login = async (email, password) => {
    const {data} = await $host.post('/auth/login', {email, password})
    return jwtDecode(data.token);
}

export const check = async () => {
    return await $host.post('/users')
}