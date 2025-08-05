// Mock data - replace with actual data fetching
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar.js";
import {Button} from "@/components/ui/button.js";
import {Badge} from "@/components/ui/badge.js";
import {STORE_ROUTE} from "@/utils/consts.js";
import {useNavigate, useParams} from "react-router-dom";
import { ShoppingCart, ArrowLeft, Calendar, User } from "lucide-react"
import {observer} from "mobx-react-lite";
import {useEffect, useState} from "react";
import {fetchProduct} from "@/http/productAPI.js";
import Loading from "@/components/Loading.jsx";

const getMockProduct = (id) => ({
    id,
    title: "Premium Wireless Headphones",
    description:
        "Experience crystal-clear audio with our premium wireless headphones. Featuring advanced noise cancellation technology, 30-hour battery life, and premium leather comfort padding. Perfect for music lovers, professionals, and anyone who demands the best audio experience. These headphones deliver rich bass, crisp highs, and balanced mids for an immersive listening experience.",
    price: 199.99,
    image: "/placeholder.svg?height=500&width=500&text=Premium+Headphones",
    category: "Electronics",
    author: {
        username: "TechGuru2024",
        avatar: "/placeholder.svg?height=40&width=40&text=TG",
    },
    uploadDate: "2024-01-15",
})

function ProductPage() {
    const mockProduct = getMockProduct(1)
    const [product, setProduct] = useState({})
    const [loading, setLoading] = useState(true)
    const navigate = useNavigate();
    const {id} = useParams();

    useEffect(() => {
        fetchProduct(id).then(data => {
            setLoading(false)
            if (data) {
                setProduct(data.data)
            } else {
                navigate(STORE_ROUTE)
            }
        })
    }, [])

    if (loading) {
        return <Loading/>
    }

    const handleAddToCart = () => {
        console.log("Added to cart:", product)
    }

    const formatDate = (dateString) => {
        return new Date(dateString).toLocaleDateString("en-US", {
            year: "numeric",
            month: "long",
            day: "numeric",
        })
    }

    return (
        <div className="container mx-auto px-4 py-8 bg-background min-h-screen text-left pt-24">
            {/* Back button */}
            <div
                className="inline-flex items-center gap-2 text-muted-foreground hover:text-foreground transition-colors duration-200 mb-8 hover:cursor-pointer"
                onClick={() => navigate(STORE_ROUTE)}
            >
                <ArrowLeft className="w-4 h-4"/>
                Back to Products
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-12">
                {/* Product Image */}
                <div className="space-y-4">
                    <div className="relative overflow-hidden rounded-2xl bg-card ring-1 ring-border">
                        <img
                            src={`${import.meta.env.VITE_API_URL}${product.previewImage}`}
                            alt={product.title}
                            width={500}
                            height={500}
                            className="w-full h-auto object-cover"
                        />
                    </div>
                </div>

                {/* Product Details */}
                <div className="space-y-6">
                    {/* Category Badge */}
                    <Badge variant="secondary" className="bg-secondary text-secondary-foreground hover:bg-secondary/80">
                        {product.category.name}
                    </Badge>

                    {/* Title */}
                    <h1 className="text-3xl lg:text-4xl font-bold text-foreground leading-tight">{product.title}</h1>

                    {/* Price */}
                    <div className="text-3xl font-bold text-foreground">${product.price.toFixed(2)}</div>

                    {/* Author Info */}
                    <div className="flex items-center gap-3 p-4 bg-card rounded-xl ring-1 ring-border">
                        <Avatar className="w-10 h-10">
                            <AvatarFallback className="bg-muted text-muted-foreground">
                                <User className="w-5 h-5"/>
                            </AvatarFallback>
                        </Avatar>
                        <div className="flex-1">
                            <p className="text-card-foreground font-medium">{product.owner.username}</p>
                            <div className="flex items-center gap-1 text-sm text-muted-foreground">
                                <Calendar className="w-3 h-3"/>
                                <span>Uploaded {formatDate(product.createdAt)}</span>
                            </div>
                        </div>
                    </div>

                    {/* Description */}
                    <div className="space-y-3">
                        <h2 className="text-xl font-semibold text-foreground">Description</h2>
                        <p className="text-muted-foreground leading-relaxed">{product.description}</p>
                    </div>

                    {/* Add to Cart Button */}
                    <div className="pt-4">
                        <Button
                            onClick={handleAddToCart}
                            size="lg"
                            className="w-full font-semibold py-4 rounded-xl flex items-center justify-center gap-3"
                        >
                            <ShoppingCart className="w-5 h-5"/>
                            Add to Cart - ${product.price.toFixed(2)}
                        </Button>
                    </div>

                    {/* Additional Product Info */}
                    <div className="grid grid-cols-2 gap-4 pt-6 border-t border-border">
                        <div className="text-center p-4 bg-card rounded-lg ring-1 ring-border">
                            <p className="text-muted-foreground text-sm">Category</p>
                            <p className="text-card-foreground font-medium">{product.category.name}</p>
                        </div>
                        <div className="text-center p-4 bg-card rounded-lg ring-1 ring-border">
                            <p className="text-muted-foreground text-sm">Product ID</p>
                            <p className="text-card-foreground font-medium">#{product.id}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default observer(ProductPage);