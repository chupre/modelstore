import {observer} from "mobx-react-lite"
import {useContext} from "react";
import {Context} from "@/main.jsx";
import BecomeSeller from "@/components/BecomeSeller.jsx";

function SellerPage() {
    const {user} = useContext(Context)
    const role = user.user.role

    return (
        (role === "SELLER" || role === "ADMIN") ? (
            <div className="mt-20">
                seller page
            </div>
        ) : (
            <BecomeSeller/>
        )
    )
}

export default observer(SellerPage)