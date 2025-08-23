import {Avatar, AvatarFallback} from "@/components/ui/avatar.js";
import {Button} from "@/components/ui/button.js";
import {Badge} from "@/components/ui/badge.js";
import {LOGIN_ROUTE, STORE_ROUTE} from "@/utils/consts.js";
import {useNavigate, useParams} from "react-router-dom";
import {
    ShoppingCart,
    ArrowLeft,
    Calendar,
    User,
    Heart,
    MessageCircle,
    Send,
} from "lucide-react"
import {observer} from "mobx-react-lite";
import {useContext, useEffect, useRef, useState} from "react";
import {
    comment, fetchComments, fetchCommentsWithUserLike, fetchProduct, fetchProductWithUserLike, likeProduct, unlikeProduct
} from "@/http/productAPI.js";
import Loading from "@/components/Loading.jsx";
import useAddToCart from "@/hooks/useAddToCart.js";
import {Context} from "@/main.jsx";
import errorToast from "@/utils/errorToast.jsx";
import {Textarea} from "@/components/ui/textarea.js";
import CommentCard from "@/components/CommentCard.jsx";
import Pages from "@/components/Pages.jsx";

function ProductPage() {
    const {cart, user} = useContext(Context);
    const [product, setProduct] = useState({})
    const [loading, setLoading] = useState(true)
    const navigate = useNavigate();
    const {id} = useParams();
    const addToCart = useAddToCart()

    const [comments, setComments] = useState([])
    const [newComment, setNewComment] = useState("")
    const commentsRef = useRef(null);
    const [totalPages, setTotalPages] = useState(0)
    const [currentPage, setCurrentPage] = useState(0)

    useEffect(() => {
        const loadData = async () => {
            try {
                const productPromise = user.isAuth ?
                    await fetchProductWithUserLike(id)
                    :
                    await fetchProduct(id);

                const commentsPromise = user.isAuth ?
                    await fetchCommentsWithUserLike(id, currentPage, 4)
                    :
                    await fetchComments(id, currentPage, 4);

                const [productRes, commentsRes] =
                    await Promise.all([
                        productPromise,
                        commentsPromise,
                    ]);

                if (productRes) {
                    setProduct(productRes.data);
                } else {
                    navigate(STORE_ROUTE);
                }

                setComments(commentsRes.data.content);
                setTotalPages(commentsRes.data.totalPages)
            } catch (err) {
                errorToast(err);
            } finally {
                setLoading(false);
            }
        };

        loadData();
    }, [id, user.isAuth, user.user?.sub, navigate, currentPage]);

    if (loading) {
        return <Loading/>
    }

    const formatDate = (dateString) => {
        return new Date(dateString).toLocaleDateString("en-US", {
            year: "numeric",
            month: "long",
            day: "numeric",
        })
    }

    const handleProductLike = async () => {
        if (!user.isAuth) {
            navigate(LOGIN_ROUTE)
            return
        }

        if (product.isLiked) {
            setProduct(prev => ({
                ...prev,
                isLiked: false,
                likesCount: prev.likesCount - 1,
            }));
            try {
                await unlikeProduct(product.id)
            } catch (e) {
                errorToast(e)
                setProduct(prev => ({
                    ...prev,
                    isLiked: true,
                    likesCount: prev.likesCount + 1,
                }));
            }
        } else {
            setProduct(prev => ({
                ...prev,
                isLiked: true,
                likesCount: prev.likesCount + 1,
            }));
            try {
                await likeProduct(product.id)
            } catch (e) {
                errorToast(e)
                setProduct(prev => ({
                    ...prev,
                    isLiked: false,
                    likesCount: prev.likesCount - 1,
                }));
            }
        }
    }

    const handleAddComment = async () => {
        if (!user.isAuth) {
            navigate(LOGIN_ROUTE)
            return
        }

        try {
            await comment(product.id, newComment).then(async () => {
                const res = user.isAuth ?
                    await fetchCommentsWithUserLike(id, currentPage, 4)
                    :
                    await fetchComments(id, currentPage, 4);
                setTotalPages(res.data.totalPages);
                setNewComment("")
                setComments(res.data.content)
            })
        } catch (e) {
            errorToast(e)
        }
    }

    return (
        <div className="container max-w-7xl mx-auto px-8 py-8 bg-background rounded-2xl text-left mt-20">
            {/* Back button */}
            <div
                className="inline-flex items-center gap-2 text-muted-foreground hover:text-foreground transition-colors duration-200 mb-8 hover:cursor-pointer"
                onClick={() => {
                    navigate(-1, {state: {backFromProductPage: true}})
                }}
            >
                <ArrowLeft className="w-4 h-4"/>
                Go Back
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

                    <div className="flex items-center gap-4 p-4 bg-card rounded-xl ring-1 ring-border">
                            <Button
                                variant="ghost"
                                size="sm"
                                onClick={handleProductLike}
                                className={`flex items-center gap-1 h-8 px-2 ${
                                    product.isLiked
                                        ? "text-red-500 hover:text-red-600"
                                        : "text-muted-foreground hover:text-foreground"
                                }`}
                            >
                                <Heart className={`w-4 h-4 ${product.isLiked ? "fill-current" : ""}`} />
                                <span className="text-sm min-w-[0.5rem] text-center">{product.likesCount}</span>
                            </Button>

                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => commentsRef.current?.scrollIntoView({ behavior: "smooth" })}
                            className="flex items-center gap-1 h-8 px-2 text-muted-foreground"
                        >
                            <MessageCircle className="w-5 h-5"/>
                            <span className="font-medium text-sm min-w-[0.5rem] text-center">{comments.length} comments</span>
                        </Button>
                    </div>

                    {/* Description */}
                    <div className="space-y-3">
                        <h2 className="text-xl font-semibold text-foreground">Description</h2>
                        <p className="text-muted-foreground leading-relaxed">{product.description}</p>
                    </div>

                    <div className="pt-4">
                        {!cart.isInCart(product.id) ?
                            <Button
                                onClick={() => addToCart(product.id)}
                                size="lg"
                                className="w-full font-semibold py-4 rounded-xl flex items-center justify-center gap-3"
                            >
                                <ShoppingCart className="w-5 h-5"/>
                                Add to Cart — ${product.price.toFixed(2)}
                            </Button>
                            :
                            <div
                                className="px-4 py-2 rounded-lg bg-secondary text-secondary-foreground flex items-center justify-center text-sm font-medium cursor-default h-10">
                                In Cart
                            </div>
                        }
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
            <div className="mx-auto p-6 space-y-6">
                <div className="flex items-center gap-2 mb-6">
                    <h2 className="text-2xl font-semibold">Comments</h2>
                    <span className="px-2 py-1 bg-muted text-muted-foreground rounded-md text-sm">{comments.length}</span>
                </div>

                {/* Add Comment Form */}
                <div ref={commentsRef} className="border rounded-lg p-4 bg-card">
                    <div className="flex gap-3">
                        <Avatar className="w-10 h-10 flex-shrink-0">
                            <AvatarFallback>
                                <User className="w-5 h-5" />
                            </AvatarFallback>
                        </Avatar>
                        <div className="flex-1 space-y-3">
                            <Textarea
                                placeholder="Write a comment..."
                                value={newComment}
                                onChange={(e) => setNewComment(e.target.value)}
                                className="min-h-[80px] resize-none"
                            />
                            <div className="flex justify-end">
                                <Button onClick={handleAddComment} disabled={!newComment.trim()} size="sm">
                                    <Send className="w-4 h-4 mr-0" />
                                    Post
                                </Button>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Comments List */}
                <div className="space-y-4">
                    {comments.map((comment) => (
                        <CommentCard
                            key={comment.id}
                            comment={comment}
                            setComments={setComments}
                            fetch={user.isAuth ?
                                () => fetchCommentsWithUserLike(comment.productId, currentPage, 4)
                                :
                                () => fetchComments(comment.productId, currentPage, 4)
                            }
                        />
                    ))}
                    <Pages totalPages={totalPages} currentPage={currentPage} setCurrentPage={setCurrentPage}/>
                </div>
            </div>
        </div>
    );
}

export default observer(ProductPage);