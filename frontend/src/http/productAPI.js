import {$host} from "@/http/index.js";

export const fetchProducts = async (page = 0, size = 10) => {
    return await $host.get(`/products`, {params: {page, size}});
}