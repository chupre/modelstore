import {useState, useRef, useEffect, useContext} from "react"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Edit2, Save, X, User, ShoppingBag, Heart, MessageSquare, Upload, Crop } from "lucide-react"
import {Context} from "@/main.jsx";
import {useParams} from "react-router-dom";
import {fetchProfile, updateProfile} from "@/http/userAPI.js";
import Loading from "@/components/Loading.jsx";
import {observer} from "mobx-react-lite";
import errorToast from "@/utils/errorToast.jsx";
import {fetchLikedProducts} from "@/http/productAPI.js";
import ProductCard from "@/components/ProductCard.jsx";
import Pages from "@/components/Pages.jsx";

function ProfilePage() {
    const {user} = useContext(Context)
    const {id} = useParams();
    const [loading, setLoading] = useState(true)

    const [profile, setProfile] = useState({
        name: "John Doe",
        avatarUrl: "/professional-headshot.png",
        bio: "Full-stack developer passionate about creating amazing user experiences. Love working with React, TypeScript, and modern web technologies.",
    })

    const [isEditing, setIsEditing] = useState(false)
    const [editedProfile, setEditedProfile] = useState(profile)
    const [activeTab, setActiveTab] = useState("orders")
    const [showCropper, setShowCropper] = useState(false)
    const [originalImage, setOriginalImage] = useState("")
    const [uploadedOriginalImage, setUploadedOriginalImage] = useState("")
    const canvasRef = useRef(null)
    const imageRef = useRef(null)
    const [cropPosition, setCropPosition] = useState({ x: 0, y: 0 })
    const [cropSize, setCropSize] = useState(200)
    const [isDragging, setIsDragging] = useState(false)
    const [dragStart, setDragStart] = useState({ x: 0, y: 0 })
    const cropperRef = useRef(null)

    const [favoriteProducts, setFavoriteProducts] = useState([])

    useEffect(() => {
        if (user.isAuth && id === user.user.sub) {
            setProfile(user.profile)
            setLoading(false)
            fetchLikedProducts(id).then((res) => {
                setFavoriteProducts(res.data.content)

            })
        } else {
            fetchProfile(id).then((res) => {
                setProfile(res.data)
                setLoading(false)
            })
        }
    }, [user.isAuth, user.profile]);

    if (loading) {
        return <Loading/>
    }

    const handleEdit = () => {
        setEditedProfile(profile)
        setIsEditing(true)
    }

    const handleSave = async () => {
        const formData = new FormData()

        if (profile.name !== editedProfile.name) {
            formData.append("username", editedProfile.name);
        }

        if (profile.bio !== editedProfile.bio) {
            formData.append("bio", editedProfile.bio);
        }

        if (editedProfile.avatarUrl?.startsWith("data:")) {
            const file = dataURLtoFile(editedProfile.avatarUrl, "avatar.png");
            formData.append("avatarImage", file);
        }

        try {
            await updateProfile(id, formData).then(() => {
                setProfile(editedProfile)
                setIsEditing(false)
            })
        } catch (e) {
            errorToast(e)
        }
    }

    const handleCancel = () => {
        setEditedProfile(profile)
        setIsEditing(false)
        setCropPosition({ x: 0, y: 0 })
        setCropSize(200)
    }

    const handleInputChange = (field, value) => {
        setEditedProfile((prev) => ({
            ...prev,
            [field]: value,
        }))
    }

    const handleAvatarUpload = (event) => {
        const file = event.target.files?.[0]
        if (file) {
            const reader = new FileReader()
            reader.onload = (e) => {
                const result = e.target?.result
                setUploadedOriginalImage(result)
                setOriginalImage(result)
                setShowCropper(true)
            }
            reader.readAsDataURL(file)
        }
    }

    const handleMouseDown = (e) => {
        setIsDragging(true)
        const rect = cropperRef.current?.getBoundingClientRect()
        if (rect) {
            setDragStart({
                x: e.clientX - rect.left - cropPosition.x,
                y: e.clientY - rect.top - cropPosition.y,
            })
        }
    }

    const handleMouseMove = (e) => {
        if (!isDragging || !cropperRef.current || !imageRef.current) return

        const rect = cropperRef.current.getBoundingClientRect()
        const imgRect = imageRef.current.getBoundingClientRect()

        const newX = e.clientX - rect.left - dragStart.x
        const newY = e.clientY - rect.top - dragStart.y

        const imageLeft = imgRect.left - rect.left
        const imageTop = imgRect.top - rect.top
        const imageRight = imageLeft + imgRect.width
        const imageBottom = imageTop + imgRect.height

        const maxX = imageRight - cropSize
        const maxY = imageBottom - cropSize

        setCropPosition({
            x: Math.max(imageLeft, Math.min(newX, maxX)),
            y: Math.max(imageTop, Math.min(newY, maxY)),
        })
    }

    const handleMouseUp = () => {
        setIsDragging(false)
    }

    const handleCropSizeChange = (newSize) => {
        setCropSize(newSize)

        if (cropperRef.current && imageRef.current) {
            const rect = cropperRef.current.getBoundingClientRect()
            const imgRect = imageRef.current.getBoundingClientRect()

            const imageLeft = imgRect.left - rect.left
            const imageTop = imgRect.top - rect.top
            const imageRight = imageLeft + imgRect.width
            const imageBottom = imageTop + imgRect.height

            const maxX = imageRight - newSize
            const maxY = imageBottom - newSize

            setCropPosition((prev) => ({
                x: Math.max(imageLeft, Math.min(prev.x, maxX)),
                y: Math.max(imageTop, Math.min(prev.y, maxY)),
            }))
        }
    }

    const cropImageToCircle = () => {
        const canvas = canvasRef.current
        const img = imageRef.current
        if (!canvas || !img || !cropperRef.current) return

        const ctx = canvas.getContext("2d")
        if (!ctx) return

        const size = 300 // Output size
        canvas.width = size
        canvas.height = size

        const rect = cropperRef.current.getBoundingClientRect()
        const scaleX = img.naturalWidth / rect.width
        const scaleY = img.naturalHeight / rect.height

        const cropX = cropPosition.x * scaleX
        const cropY = cropPosition.y * scaleY
        const cropWidth = cropSize * scaleX
        const cropHeight = cropSize * scaleY

        ctx.beginPath()
        ctx.arc(size / 2, size / 2, size / 2, 0, Math.PI * 2)
        ctx.clip()

        ctx.drawImage(img, cropX, cropY, cropWidth, cropHeight, 0, 0, size, size)

        const croppedDataUrl = canvas.toDataURL("image/png")
        handleInputChange("avatarUrl", croppedDataUrl)
        setShowCropper(false)
        setOriginalImage("")
        setCropPosition({ x: 0, y: 0 })
        setCropSize(200)
    }

    const getAvatarSrc = (url) => {
        if (!url) return "";
        if (url.startsWith("http") || url.startsWith("data:")) {
            return url;
        }
        return `${import.meta.env.VITE_API_URL}${url}`;
    };

    function dataURLtoFile(dataUrl, filename) {
        const arr = dataUrl.split(',');
        const mime = arr[0].match(/:(.*?);/)[1];
        const bstr = atob(arr[1]);
        let n = bstr.length;
        const u8arr = new Uint8Array(n);
        while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
        }
        return new File([u8arr], filename, { type: mime });
    }

    return (
        <div className="min-h-screen mt-8">
            <div className="mx-auto max-w-7xl p-6 md:p-8">
                {showCropper && (
                    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
                        <Card className="w-[500px] max-w-[90vw]">
                            <CardHeader>
                                <h3 className="text-lg font-semibold">Crop Your Avatar</h3>
                                <p className="text-sm text-muted-foreground">Drag the circle to position your crop area</p>
                            </CardHeader>
                            <CardContent className="space-y-4">
                                <div className="flex justify-center">
                                    <div
                                        ref={cropperRef}
                                        className="relative inline-block cursor-crosshair select-none"
                                        onMouseMove={handleMouseMove}
                                        onMouseUp={handleMouseUp}
                                        onMouseLeave={handleMouseUp}
                                    >
                                        <img
                                            ref={imageRef}
                                            src={originalImage || "/placeholder.svg"}
                                            alt="Original"
                                            className="max-w-80 max-h-80 object-contain"
                                            onLoad={() => {
                                                if (cropperRef.current) {
                                                    const rect = cropperRef.current.getBoundingClientRect()
                                                    setCropPosition({
                                                        x: (rect.width - cropSize) / 2,
                                                        y: (rect.height - cropSize) / 2,
                                                    })
                                                }
                                            }}
                                        />
                                        <div
                                            className="absolute border-2 border-primary rounded-full bg-primary/10 cursor-move"
                                            style={{
                                                left: cropPosition.x,
                                                top: cropPosition.y,
                                                width: cropSize,
                                                height: cropSize,
                                            }}
                                            onMouseDown={handleMouseDown}
                                        >
                                            <div className="absolute inset-0 rounded-full border-2 border-white/50" />
                                        </div>
                                        <div
                                            className="absolute inset-0 bg-black/40 pointer-events-none"
                                            style={{
                                                clipPath: `circle(${cropSize / 2}px at ${cropPosition.x + cropSize / 2}px ${cropPosition.y + cropSize / 2}px)`,
                                                clipRule: "evenodd",
                                                background: "transparent",
                                            }}
                                        />
                                    </div>
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="cropSize" className="text-sm font-medium">
                                        Crop Size
                                    </Label>
                                    <Input
                                        id="cropSize"
                                        type="range"
                                        min="100"
                                        max="300"
                                        value={cropSize}
                                        onChange={(e) => handleCropSizeChange(Number(e.target.value))}
                                        className="w-full"
                                    />
                                </div>

                                <canvas ref={canvasRef} className="hidden" />
                                <div className="flex gap-2 justify-end">
                                    <Button
                                        variant="outline"
                                        onClick={() => {
                                            setShowCropper(false)
                                            setOriginalImage("")
                                            setCropPosition({ x: 0, y: 0 })
                                            setCropSize(200)
                                        }}
                                    >
                                        Cancel
                                    </Button>
                                    <Button onClick={cropImageToCircle} className="gap-2">
                                        <Crop className="h-4 w-4" />
                                        Apply Crop
                                    </Button>
                                </div>
                            </CardContent>
                        </Card>
                    </div>
                )}

                <div className="mb-8">
                    <Card className="border-border bg-card/0 backdrop-blur-lg">
                        <CardHeader className="relative pb-6">
                            <div className="absolute right-6 top-6">
                                {user.user.sub === id && (
                                    !isEditing ? (
                                        <Button variant="outline" size="sm" onClick={handleEdit} className="gap-2 bg-transparent">
                                            <Edit2 className="h-4 w-4" />
                                            Edit Profile
                                        </Button>
                                    ) : (
                                        <div className="flex gap-2">
                                            <Button variant="outline" size="sm" onClick={handleCancel} className="gap-2 bg-transparent">
                                                <X className="h-4 w-4" />
                                                Cancel
                                            </Button>
                                            <Button size="sm" onClick={handleSave} className="gap-2">
                                                <Save className="h-4 w-4" />
                                                Save
                                            </Button>
                                        </div>
                                    )
                                )}
                            </div>

                            <div className="flex flex-col md:flex-row items-center md:items-start gap-6">
                                <div className="relative">
                                    <Avatar className="h-32 w-32 border-2 border-border">
                                        <AvatarImage
                                            src={isEditing ? getAvatarSrc(editedProfile.avatarUrl) : getAvatarSrc(profile.avatarUrl)}
                                            alt={isEditing ? editedProfile.name : profile.name}
                                        />
                                        <AvatarFallback className="bg-muted text-3xl">
                                            <User className="h-16 w-16" />
                                        </AvatarFallback>
                                    </Avatar>
                                    {isEditing && uploadedOriginalImage && (
                                        <Button
                                            type="button"
                                            variant="secondary"
                                            size="sm"
                                            onClick={() => {
                                                setOriginalImage(uploadedOriginalImage)
                                                setShowCropper(true)
                                            }}
                                            className="absolute -bottom-2 left-1/2 transform -translate-x-1/2 gap-1 text-xs px-2 py-1 h-7"
                                        >
                                            <Crop className="h-3 w-3" />
                                            Re-crop
                                        </Button>
                                    )}
                                </div>

                                <div className="flex-1 text-center md:text-left">
                                    {!isEditing ? (
                                        <div className="space-y-2">
                                            <h1 className="text-3xl font-bold text-foreground">{profile.name}</h1>
                                            <p className="text-muted-foreground leading-relaxed max-w-2xl">
                                                {profile.bio || "No bio available."}
                                            </p>
                                        </div>
                                    ) : (
                                        <div className="space-y-4 max-w-md">
                                            <div className="space-y-2">
                                                <Label htmlFor="name" className="text-sm font-medium">
                                                    Name
                                                </Label>
                                                <Input
                                                    id="name"
                                                    value={editedProfile.name}
                                                    onChange={(e) => handleInputChange("name", e.target.value)}
                                                    placeholder="Enter your name"
                                                    className="bg-background"
                                                />
                                            </div>
                                            <div className="space-y-2">
                                                <Label htmlFor="avatar" className="text-sm font-medium">
                                                    Avatar
                                                </Label>
                                                <div className="flex items-center gap-3">
                                                    <Input
                                                        id="avatar"
                                                        type="file"
                                                        accept="image/*"
                                                        onChange={handleAvatarUpload}
                                                        className="bg-background file:mr-3 file:py-1 file:px-3 file:rounded-md file:border-0 file:text-sm file:font-medium file:bg-primary file:text-primary-foreground hover:file:bg-primary/90"
                                                    />
                                                    <Upload className="h-4 w-4 text-muted-foreground" />
                                                </div>
                                            </div>
                                            <div className="space-y-2">
                                                <Label htmlFor="bio" className="text-sm font-medium">
                                                    Bio
                                                </Label>
                                                <Textarea
                                                    id="bio"
                                                    value={editedProfile.bio}
                                                    onChange={(e) => handleInputChange("bio", e.target.value)}
                                                    placeholder="Tell us about yourself..."
                                                    className="min-h-[100px] bg-background resize-none"
                                                />
                                            </div>
                                        </div>
                                    )}
                                </div>
                            </div>
                        </CardHeader>
                    </Card>
                </div>

                <div className="flex gap-6">
                    <div className="w-64 flex-shrink-0">
                        <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full bg-card/0" orientation="vertical">
                            <TabsList className="flex flex-col h-auto w-full p-1 border border-border bg-card/0 backdrop-blur-lg">
                                <TabsTrigger
                                    value="orders"
                                    className="w-full justify-start gap-2 hover:bg-muted transition-colors"
                                >
                                    <ShoppingBag className="h-4 w-4" />
                                    Orders
                                </TabsTrigger>
                                <TabsTrigger
                                    value="liked"
                                    className="w-full justify-start gap-2 hover:bg-muted transition-colors"
                                >
                                    <Heart className="h-4 w-4" />
                                    Liked Products
                                </TabsTrigger>
                                <TabsTrigger
                                    value="comments"
                                    className="w-full justify-start gap-2 hover:bg-muted transition-colors"
                                >
                                    <MessageSquare className="h-4 w-4" />
                                    Comments
                                </TabsTrigger>
                            </TabsList>
                        </Tabs>
                    </div>

                    <div className="flex-1">
                        <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full">
                            <TabsContent value="orders" className="space-y-4 mt-0">
                                <Card className="border-border bg-card/0 backdrop-blur-lg">
                                    <CardContent className="p-8 text-center">
                                        <ShoppingBag className="h-12 w-12 mx-auto mb-4 text-muted-foreground" />
                                        <h3 className="text-lg font-semibold mb-2">Your Orders</h3>
                                        <p className="text-muted-foreground">Order history will be displayed here</p>
                                    </CardContent>
                                </Card>
                            </TabsContent>

                            <TabsContent value="liked" className="space-y-4 mt-0">
                                <Card className="border-border bg-card/0 backdrop-blur-lg">
                                    <CardContent className="p-8 text-center">
                                        {favoriteProducts ?
                                            <div>
                                                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
                                                    {favoriteProducts.map((product) => (
                                                        <ProductCard key={product.id} product={product}></ProductCard>
                                                    ))}
                                                </div>
                                                <Pages/>
                                            </div>
                                        :
                                        <div>
                                            <Heart className="h-12 w-12 mx-auto mb-4 text-muted-foreground" />
                                            <h3 className="text-lg font-semibold mb-2">Liked Products</h3>
                                            <p className="text-muted-foreground">Your favorite products will appear here</p>
                                        </div>
                                        }
                                    </CardContent>
                                </Card>
                            </TabsContent>

                            <TabsContent value="comments" className="space-y-4 mt-0">
                                <Card className="border-border bg-card/0 backdrop-blur-lg">
                                    <CardContent className="p-8 text-center">
                                        <MessageSquare className="h-12 w-12 mx-auto mb-4 text-muted-foreground" />
                                        <h3 className="text-lg font-semibold mb-2">Your Comments</h3>
                                        <p className="text-muted-foreground">Comments you've left on products will be shown here</p>
                                    </CardContent>
                                </Card>
                            </TabsContent>
                        </Tabs>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default observer(ProfilePage)