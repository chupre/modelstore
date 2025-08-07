import {useContext} from "react";
import {Context} from "@/main.jsx";
import {useNavigate} from "react-router-dom";
import {LOGIN_ROUTE} from "@/utils/consts.js";
import {addCartItem} from "@/http/cartAPI.js";

function useAddToCart() {
    const { user, cart } = useContext(Context);
    const navigate = useNavigate();

    return (productId) => {
        if (!user.isAuth) {
            navigate(LOGIN_ROUTE);
        } else {
            addCartItem(cart.cart.id, productId).then((res) => {
                cart.addCartItem(res.data);
            })
        }
    }
}

export default useAddToCart;