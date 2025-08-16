import {Card, CardContent, CardFooter} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {Heart, ShoppingCart, User} from "lucide-react"
import {observer} from "mobx-react-lite";
import {useNavigate} from "react-router-dom";
import {PRODUCT_ROUTE} from "@/utils/consts.js";
import useAddToCart from "@/hooks/useAddToCart.js";
import {useContext, useState} from "react";
import {Context} from "@/main.jsx";
import {likeProduct, unlikeProduct} from "@/http/productAPI.js";
import errorToast from "@/utils/errorToast.jsx";

function ProductCard({product, isLikedInitial}) {
    const {cart} = useContext(Context);
    const navigate = useNavigate();

    const [isLiked, setIsLiked] = useState(isLikedInitial)
    const [likesCount, setLikesCount] = useState(product.likesCount)

    const handleLike = async () => {
        if (isLiked) {
            setIsLiked(false)
            setLikesCount((prev) => prev - 1)
            try {
                await unlikeProduct(product.id)
            } catch (e) {
                errorToast(e)
                setIsLiked(true)
                setLikesCount((prev) => prev + 1)
            }
        } else {
            setIsLiked(true)
            setLikesCount((prev) => prev + 1)
            try {
                await likeProduct(product.id)
            } catch (e) {
                errorToast(e)
                setIsLiked(false)
                setLikesCount((prev) => prev - 1)
            }
        }
    }

    const addToCart = useAddToCart()

    return (
        <Card
            className="group w-full max-w-sm overflow-hidden transition-all duration-300 rounded-xl p-0 gap-4 bg-secondary/20 backdrop-blur-lg border border-white/10 hover:border-white/20">

            <div
                className="relative w-full h-86 overflow-hidden rounded-t-xl hover:cursor-pointer pb-0 mb-0"
                onClick={() => navigate(PRODUCT_ROUTE + '/' + product.id)}
            >
                <img
                    src={`${import.meta.env.VITE_API_URL}${product.previewImage}`}
                    alt={product.title}
                    className="w-full h-full object-cover transition-transform duration-300 group-hover:scale-105"
                />
                <div className="absolute inset-0 bg-black/0 group-hover:bg-black/5 transition-colors duration-300"/>
            </div>

            <CardContent>
                <h3 className="text-xl font-semibold text-gray-300 line-clamp-2 group-hover:text-white transition-colors duration-200 hover:cursor-pointer text-left w-fit"
                    onClick={() => navigate(PRODUCT_ROUTE + '/' + product.id)}
                >
                    {product.title}
                </h3>

                <div className="flex items-center justify-between text-sm text-muted-foreground">
                    <div className="flex items-center gap-1">
                        <User className="w-4 h-4" />
                        <span>{product.owner.username}</span>
                    </div>

                    <Button size="sm" variant="ghost" className="p-1 h-auto hover:bg-transparent" onClick={handleLike}>
                        <div className="flex items-center gap-1">
                            <Heart
                                className={`w-4 h-4 transition-colors ${isLiked ? "fill-red-500 text-red-500" : "text-muted-foreground hover:text-red-400"}`}
                            />
                            <span className="text-xs">{likesCount}</span>
                        </div>
                    </Button>
                </div>
            </CardContent>

            <CardFooter className="flex justify-between items-center px-4 pb-4 pt-0">
                <span className="text-lg font-bold ml-2">${product.price.toFixed(2)}</span>
                {!cart.isInCart(product.id) ?
                    <Button
                        size="sm"
                        onClick={() => addToCart(product.id)}
                        className="px-4 py-2 rounded-lg transition-colors duration-200 flex items-center gap-2"
                    >
                        <ShoppingCart className="w-4 h-4"/>
                        Add to Cart
                    </Button>
                    :
                    <div className="px-4 py-2 rounded-lg backdrop-contrast-80 text-secondary-foreground flex items-center text-sm font-medium cursor-default h-8">
                        In Cart
                    </div>
                }
            </CardFooter>
        </Card>
    );
}

export default observer(ProductCard);
