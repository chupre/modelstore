import {useContext, useEffect, useState} from "react";
import {Context} from "@/main.jsx";
import BecomeSeller from "@/components/BecomeSeller.jsx";
import SellerDashboard from "@/components/SellerDashboard.jsx";
import {fetchCurrentSeller} from "@/http/sellerAPI.js";
import Loading from "@/components/Loading.jsx";

function SellerPage() {
    const {user} = useContext(Context)
    const role = user.user.role

    const [isAdminWithSeller, setIsAdminWithSeller] = useState(false)
    const [isLoading, setIsLoading] = useState(false)

    useEffect(() => {
        if (role === "ADMIN") {
            setIsLoading(true)
            fetchCurrentSeller().then((res) => {
                if (res) {
                    setIsAdminWithSeller(true)
                    setIsLoading(false)
                }
            })
        }
    }, []);

    if (isLoading) {
        return <Loading/>
    }

    return (
        (role === "SELLER" || isAdminWithSeller) ? (
            <SellerDashboard/>
        ) : (
            <BecomeSeller/>
        )
    )
}

export default SellerPage