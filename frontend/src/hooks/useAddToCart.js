import {useContext} from "react";
import {Context} from "@/main.jsx";
import {useNavigate} from "react-router-dom";
import {LOGIN_ROUTE} from "@/utils/consts.js";
import {addCartItem} from "@/http/cartAPI.js";
import errorToast from "@/utils/errorToast.jsx";
import {toast} from "sonner";

function useAddToCart() {
    const { user, cart } = useContext(Context);
    const navigate = useNavigate();

    return (productId) => {
        if (!user.isAuth) {
            navigate(LOGIN_ROUTE);
        } else {
            addCartItem(cart.cart.id, productId).then((res) => {
                cart.addCartItem(res.data);
                toast.success("Added item to card")
            })
            .catch(errorToast)
        }
    }
}

export default useAddToCart;