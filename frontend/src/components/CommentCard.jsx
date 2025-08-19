import {useContext, useState} from "react";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar.js";
import { Button } from "@/components/ui/button.js";
import { Textarea } from "@/components/ui/textarea.js";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "@/components/ui/alert-dialog.js";
import { User, Edit2, Trash2, Check, X, Heart } from "lucide-react";
import {
    likeComment,
    unlikeComment,
    editComment,
    deleteComment,
} from "@/http/productAPI.js";
import errorToast from "@/utils/errorToast.jsx";
import {Context} from "@/main.jsx";
import {LOGIN_ROUTE, PROFILE_ROUTE} from "@/utils/consts.js";
import {useNavigate} from "react-router-dom";

function CommentCard({  comment,
                        setComments,
                        showLiked = true,
                        fetch
                     }) {
    const {user} = useContext(Context)
    const [isEditing, setIsEditing] = useState(false);
    const [editingText, setEditingText] = useState(comment.comment);

    const navigate = useNavigate();

    const formatDate = (dateString) =>
        new Date(dateString).toLocaleDateString("en-US", {
            year: "numeric",
            month: "long",
            day: "numeric",
        });

    const handleCommentLike = async () => {
        if (!user.isAuth) {
            navigate(LOGIN_ROUTE)
            return
        }

        const isLiked = comment.isLiked;
        const updated = {
            ...comment,
            isLiked: !isLiked,
            likes: comment.likes + (isLiked ? -1 : 1),
        };

        setComments((prev) =>
            prev.map((c) => (c.id === comment.id ? updated : c))
        );

        try {
            if (isLiked) await unlikeComment(comment.id);
            else await likeComment(comment.id);
        } catch (e) {
            errorToast(e);
            setComments((prev) => prev.map((c) => (c.id === comment.id ? comment : c)));
        }
    };

    const handleSaveEdit = async () => {
        if (!editingText.trim()) return;
        try {
            await editComment(comment.id, editingText);
            const res = await fetch();
            setComments(res.data.content);
            setIsEditing(false);
        } catch (e) {
            errorToast(e);
        }
    };

    const handleDelete = async () => {
        try {
            await deleteComment(comment.id);
            const res = await fetch();
            setComments(res.data.content);
        } catch (e) {
            errorToast(e);
        }
    };

    return (
        <div className="border rounded-lg p-4 bg-card group">
            <div className="flex gap-3">
                <Avatar
                    onClick={() => comment?.user && navigate(PROFILE_ROUTE + '/' + comment.user.id)}
                    className="hover:cursor-pointer"
                >
                    {comment?.user?.avatarUrl ? (
                        <AvatarImage
                            src={`${import.meta.env.VITE_API_URL}${comment.user.avatarUrl}`}
                            alt="User"
                        />
                    ) : null}
                    <AvatarFallback>
                        <User />
                    </AvatarFallback>
                </Avatar>

                <div className="flex-1 space-y-2">
                    <div className="flex items-center justify-between">
                        <div className="flex items-center gap-2">
                            <span
                                className="font-medium px-1 rounded hover:cursor-pointer hover:backdrop-brightness-125 transition"
                                onClick={() => navigate(PROFILE_ROUTE + '/' + comment.user.id)}
                            >
                              {comment.user?.username}
                            </span>

                            <span className="text-sm text-muted-foreground">
                {formatDate(comment.createdAt)}
              </span>
                        </div>

                        {user.user && parseInt(user.user.sub) === comment.userId && !isEditing && (
                            <div className="flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                                <Button
                                    variant="ghost"
                                    size="sm"
                                    onClick={() => setIsEditing(true)}
                                    className="h-8 w-8 p-0"
                                >
                                    <Edit2 className="h-4 w-4" />
                                    <span className="sr-only">Edit comment</span>
                                </Button>

                                <AlertDialog>
                                    <AlertDialogTrigger asChild>
                                        <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                                            <Trash2 className="h-4 w-4 text-destructive" />
                                        </Button>
                                    </AlertDialogTrigger>

                                    <AlertDialogContent>
                                        <AlertDialogHeader>
                                            <AlertDialogTitle>Are you sure?</AlertDialogTitle>
                                        </AlertDialogHeader>
                                        <AlertDialogFooter>
                                            <AlertDialogCancel>Cancel</AlertDialogCancel>
                                            <AlertDialogAction onClick={handleDelete}>
                                                Continue
                                            </AlertDialogAction>
                                        </AlertDialogFooter>
                                    </AlertDialogContent>
                                </AlertDialog>
                            </div>
                        )}
                    </div>

                    {isEditing ? (
                        <div className="space-y-3">
                            <Textarea
                                value={editingText}
                                onChange={(e) => setEditingText(e.target.value)}
                                className="min-h-[80px] resize-none"
                            />
                            <div className="flex gap-2">
                                <Button
                                    size="sm"
                                    onClick={handleSaveEdit}
                                    disabled={!editingText.trim()}
                                >
                                    <Check className="w-4 h-4 mr-1" />
                                    Save
                                </Button>
                                <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={() => setIsEditing(false)}
                                >
                                    <X className="w-4 h-4 mr-1" />
                                    Cancel
                                </Button>
                            </div>
                        </div>
                    ) : (
                        <p className="text-sm leading-relaxed">{comment.comment}</p>
                    )}

                    {showLiked &&
                        <div className="flex items-center justify-between pt-2">
                            <div></div>
                            <Button
                                variant="ghost"
                                size="sm"
                                onClick={handleCommentLike}
                                className={`flex items-center gap-1 h-8 px-2 ${
                                    comment.isLiked
                                        ? "text-red-500 hover:text-red-600"
                                        : "text-muted-foreground hover:text-foreground"
                                }`}
                            >
                                <Heart
                                    className={`w-4 h-4 ${comment.isLiked ? "fill-current" : ""}`}
                                />
                                <span className="text-sm">{comment.likes}</span>
                            </Button>
                        </div>
                    }
                </div>
            </div>
        </div>
    );
}

export default CommentCard;
