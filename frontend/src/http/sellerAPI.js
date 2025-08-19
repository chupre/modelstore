import {$authHost} from "@/http/index.js";

export const becomeSeller = async (userId, payoutMethod, payoutDestination) => {
    await $authHost.post(`/sellers`, {userId, payoutMethod, payoutDestination})
}