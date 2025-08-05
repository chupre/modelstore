import {Card, CardContent, CardFooter} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import { ShoppingCart } from "lucide-react"
import {observer} from "mobx-react-lite";
import {useNavigate} from "react-router-dom";
import {PRODUCT_ROUTE} from "@/utils/consts.js";

export function ProductCard({product}) {
    const navigate = useNavigate();

    const handleAddToCart = () => {
        console.log("add to cart")
    };

    return (
        <Card className="group w-full max-w-sm overflow-hidden shadow-sm hover:shadow-lg transition-all duration-300 rounded-xl p-0 gap-4">
            <div
                className="relative w-full h-86 overflow-hidden rounded-t-xl hover:cursor-pointer pb-0 mb-0"
                onClick={() => navigate(PRODUCT_ROUTE + '/' + product.id)}
            >
                <img
                    src={`${import.meta.env.VITE_API_URL}${product.previewImage}`}
                    alt={product.title}
                    className="w-full h-full object-cover transition-transform duration-300 group-hover:scale-105"
                />
                <div className="absolute inset-0 bg-black/0 group-hover:bg-black/5 transition-colors duration-300" />
            </div>

            <CardContent
                onClick={() => navigate(PRODUCT_ROUTE + '/' + product.id)}
            >
                <h3 className="text-lg font-semibold text-gray-300 line-clamp-2 group-hover:text-white transition-colors duration-200 hover:cursor-pointer">
                    {product.title}
                </h3>
            </CardContent>

            <CardFooter className="flex justify-between items-center px-4 pb-4 pt-0">
                <span className="text-xl font-bold">${product.price.toFixed(2)}</span>
                <Button
                    size="sm"
                    onClick={handleAddToCart}
                    className="px-4 py-2 rounded-lg transition-colors duration-200 flex items-center gap-2"
                >
                    <ShoppingCart className="w-4 h-4" />
                    Add to Cart
                </Button>
            </CardFooter>
        </Card>
    );
}

export default observer(ProductCard);
