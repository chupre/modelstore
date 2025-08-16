import {Avatar, AvatarFallback} from "@/components/ui/avatar.js";
import {Button} from "@/components/ui/button.js";
import {Badge} from "@/components/ui/badge.js";
import {STORE_ROUTE} from "@/utils/consts.js";
import {useNavigate, useParams} from "react-router-dom";
import {ShoppingCart, ArrowLeft, Calendar, User, Heart, MessageCircle, Send} from "lucide-react"
import {observer} from "mobx-react-lite";
import {useContext, useEffect, useState} from "react";
import {
    comment,
    fetchComments, fetchLikedComments,
    fetchLikedProducts,
    fetchProduct, likeComment,
    likeProduct, unlikeComment,
    unlikeProduct
} from "@/http/productAPI.js";
import Loading from "@/components/Loading.jsx";
import useAddToCart from "@/hooks/useAddToCart.js";
import {Context} from "@/main.jsx";
import errorToast from "@/utils/errorToast.jsx";
import {Card, CardContent} from "@/components/ui/card.js";
import {Textarea} from "@/components/ui/textarea.js";

function ProductPage() {
    const {cart, user} = useContext(Context);
    const [product, setProduct] = useState({})
    const [loading, setLoading] = useState(true)
    const navigate = useNavigate();
    const {id} = useParams();
    const addToCart = useAddToCart()

    const [productLikes, setProductLikes] = useState([]);
    const [isProductLiked, setIsProductLiked] = useState(false)
    const [commentLikes, setCommentLikes] = useState([]);
    const [comments, setComments] = useState([])
    const [newComment, setNewComment] = useState("")

    useEffect(() => {
        const loadData = async () => {
            try {
                const productPromise = fetchProduct(id);
                const commentsPromise = fetchComments(id);

                let likedProductsPromise = Promise.resolve(null);
                let likedCommentsPromise = Promise.resolve(null);

                if (user.isAuth) {
                    likedProductsPromise = fetchLikedProducts(user.user.sub);
                    likedCommentsPromise = fetchLikedComments(user.user.sub, id);
                }

                const [productRes, commentsRes, likedProductsRes, likedCommentsRes] =
                    await Promise.all([
                        productPromise,
                        commentsPromise,
                        likedProductsPromise,
                        likedCommentsPromise,
                    ]);

                if (productRes) {
                    setProduct(productRes.data);
                } else {
                    navigate(STORE_ROUTE);
                }

                setComments(commentsRes.data.content);

                if (likedProductsRes) {
                    setProductLikes(likedProductsRes.data.productId);
                    setIsProductLiked(
                        likedProductsRes.data.productId.includes(parseInt(id))
                    );
                }

                if (likedCommentsRes) {
                    setCommentLikes(likedCommentsRes.data.commentId);
                }
            } catch (err) {
                errorToast(err);
            } finally {
                setLoading(false);
            }
        };

        loadData();
    }, [id, user.isAuth, user.user?.sub, navigate]);

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
        if (isProductLiked) {
            setIsProductLiked(false)
            product.likesCount--;
            setProduct(product)
            try {
                await unlikeProduct(product.id)
            } catch (e) {
                errorToast(e)
                setIsProductLiked(true)
                product.likesCount++;
                setProduct(product)
            }
        } else {
            setIsProductLiked(true)
            product.likesCount++;
            setProduct(product)
            try {
                await likeProduct(product.id)
            } catch (e) {
                errorToast(e)
                setIsProductLiked(false)
                product.likesCount--;
                setProduct(product)
            }
        }
    }

    const handleAddComment = async () => {
        try {
            await comment(product.id, newComment).then(() => {
                fetchComments(product.id).then((res) => {
                    setComments(res.data.content)
                    setNewComment("")
                })
            })
        } catch (e) {
            errorToast(e)
        }
    }

    const isCommentLiked = (id) => {
        return commentLikes.includes(id)
    }

    const handleCommentLike = async (comment) => {
        const isLiked = isCommentLiked(comment.id);

        // Optimistic update
        setComments(prev =>
            prev.map(c =>
                c.id === comment.id
                    ? { ...c, likes: c.likes + (isLiked ? -1 : 1) }
                    : c
            )
        );

        if (isLiked) {
            setCommentLikes(prev => prev.filter(item => item !== comment.id));
            try {
                await unlikeComment(comment.id);
            } catch (e) {
                errorToast(e);
                // rollback on error
                setComments(prev =>
                    prev.map(c =>
                        c.id === comment.id ? { ...c, likes: c.likes + 1 } : c
                    )
                );
                setCommentLikes(prev => [...prev, comment.id]);
            }
        } else {
            setCommentLikes(prev => [...prev, comment.id]);
            try {
                await likeComment(comment.id);
            } catch (e) {
                errorToast(e);
                // rollback on error
                setComments(prev =>
                    prev.map(c =>
                        c.id === comment.id ? { ...c, likes: c.likes - 1 } : c
                    )
                );
                setCommentLikes(prev => prev.filter(item => item !== comment.id));
            }
        }
    };

    function handleEditComment(comment) {
        
    }

    function handleDeleteComment(id) {
        
    }

    return (
        <div className="container max-w-7xl mx-auto px-8 py-8 bg-background rounded-2xl text-left mt-20">
            {/* Back button */}
            <div
                className="inline-flex items-center gap-2 text-muted-foreground hover:text-foreground transition-colors duration-200 mb-8 hover:cursor-pointer"
                onClick={() => {
                    navigate(STORE_ROUTE, {state: {backFromProductPage: true}})
                }}
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

                    <div className="flex items-center gap-4 p-4 bg-card rounded-xl ring-1 ring-border">
                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={handleProductLike}
                            className="flex items-center gap-2 hover:bg-muted text-muted-foreground"
                        >
                            <Heart
                                className={`w-5 h-5 transition-colors ${isProductLiked ? "fill-red-500 text-red-500" : "text-muted-foreground"}`}
                            />
                            <span className="font-medium">{product.likesCount}</span>
                        </Button>

                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={handleAddComment}
                            className="flex items-center gap-2 hover:bg-muted text-muted-foreground"
                        >
                            <MessageCircle className="w-5 h-5"/>
                            <span className="font-medium">{comments.length} comments</span>
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
            <div className="mt-16 space-y-8 p-10">
                <div className="border-t border-border pt-8">
                    <h2 className="text-2xl font-bold text-foreground mb-6">Comments ({comments.length})</h2>

                    {/* Add Comment Form */}
                    <Card className="mb-8">
                        <CardContent className="p-6">
                            <div className="flex gap-4">
                                <Avatar className="w-10 h-10 flex-shrink-0">
                                    <AvatarFallback className="bg-muted text-muted-foreground">
                                        <User className="w-5 h-5"/>
                                    </AvatarFallback>
                                </Avatar>
                                <div className="flex-1 space-y-4">
                                    <Textarea
                                        placeholder="Share your thoughts about this product..."
                                        value={newComment}
                                        onChange={(e) => setNewComment(e.target.value)}
                                        className="min-h-[100px] resize-none"
                                    />
                                    <div className="flex justify-end">
                                        <Button onClick={handleAddComment} disabled={!newComment.trim()}>
                                            <Send className="w-4 h-4 mr-2"/>
                                            Comment
                                        </Button>
                                    </div>
                                </div>
                            </div>
                        </CardContent>
                    </Card>

                    {/* Comments List */}
                    <div className="space-y-6">
                        {comments.map((comment) => (
                            <Card key={comment.id}>
                                <CardContent className="p-6 py-2 relative">
                                    <div className="flex gap-4">
                                        {/* Avatar */}
                                        <Avatar className="w-10 h-10 flex-shrink-0">
                                            <AvatarFallback className="bg-muted text-muted-foreground">
                                                <User className="w-5 h-5"/>
                                            </AvatarFallback>
                                        </Avatar>

                                        {/* Comment content */}
                                        <div className="flex-1 space-y-3">
                                            <div className="flex items-center justify-between">
                                                <div className="flex items-center gap-2">
                                                    <span className="font-medium text-foreground">@{comment.userId}</span>
                                                    <span className="text-sm text-muted-foreground">
                            {formatDate(comment.createdAt)}
                        </span>
                                                </div>

                                                {/* Show edit/delete if user owns the comment */}
                                                {parseInt(user.user?.sub) === comment.userId && (
                                                    <div className="flex gap-2">
                                                        <Button
                                                            variant="ghost"
                                                            size="sm"
                                                            onClick={() => handleEditComment(comment)}
                                                            className="text-muted-foreground hover:text-foreground"
                                                        >
                                                            Edit
                                                        </Button>
                                                        <Button
                                                            variant="ghost"
                                                            size="sm"
                                                            onClick={() => handleDeleteComment(comment.id)}
                                                            className="text-destructive hover:bg-destructive/10"
                                                        >
                                                            Delete
                                                        </Button>
                                                    </div>
                                                )}
                                            </div>

                                            <p className="text-muted-foreground leading-relaxed">
                                                {comment.comment}
                                            </p>
                                        </div>
                                    </div>

                                    {/* Fixed bottom-right like button */}
                                    <div className="absolute -bottom-2 right-4">
                                        <Button
                                            variant="ghost"
                                            size="sm"
                                            onClick={() => handleCommentLike(comment)}
                                            className="flex items-center gap-2 hover:bg-muted"
                                        >
                                            <Heart
                                                className={`w-4 h-4 transition-colors ${
                                                    isCommentLiked(comment.id)
                                                        ? "fill-red-500 text-red-500"
                                                        : "text-muted-foreground"
                                                }`}
                                            />
                                            <span className="text-sm">{comment.likes}</span>
                                        </Button>
                                    </div>
                                </CardContent>
                            </Card>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default observer(ProductPage);