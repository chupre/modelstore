import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { useLocation, useNavigate } from "react-router-dom";
import { HOME_ROUTE, LOGIN_ROUTE, REGISTRATION_ROUTE } from "../utils/consts";
import { login, registration } from "@/http/userAPI.js";
import { useContext, useState } from "react";
import { observer } from "mobx-react-lite";
import { Context } from "@/main.jsx";
import { toast } from "sonner";
import {AlertCircleIcon} from "lucide-react";

function Auth() {
  const { user } = useContext(Context);
  const location = useLocation();
  const isLogin = location.pathname === LOGIN_ROUTE;
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [username, setUsername] = useState("");

  const signIn = async (email, username, password) => {
    try {
      let data;
      if (isLogin) {
        data = await login(email, password);
      } else {
        data = await registration(username, email, password);
      }
      user.setUser(data);
      user.setIsAuth(true);
      navigate(HOME_ROUTE);
    } catch (error) {
      const responseData = error?.response?.data;
      if (responseData && typeof responseData === "object") {
        const messages = Object.values(responseData);

        toast('', {
          description: (
              <div className="space-y-1">
                <div className="flex items-center gap-x-2">
                  <AlertCircleIcon/>
                  <p className="text-sm font-bold">Unable to sign {isLogin ? "in" : "up"}</p>
                </div>
                <p>Please verify your credentials and try again.</p>
                <ul className="list-disc list-inside text-muted-foreground space-y-1">
                  {messages.map((msg, i) => (
                      <li key={i}>{msg}</li>
                  ))}
                </ul>
              </div>
          ),
          className: "text-left max-w-sm", // force left alignment + reasonable width
          duration: 5000,
          action: {
            label: "OK",
          }
        });

      } else {
        toast.error("Unexpected error", {
          description: "Something went wrong. Please try again.",
        });
      }
    }
  };

  return (
      <div className="w-full min-h-screen flex items-center justify-center bg-background px-4 pt-20">
        <Card className="w-full max-w-md p-4 -mt-30">
          <CardHeader>
            <CardTitle className="text-2xl text-center">
              Sign {isLogin ? "In" : "Up"}
            </CardTitle>
          </CardHeader>

          <CardContent className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <Input
                  id="email"
                  type="email"
                  placeholder="you@example.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
              />
            </div>

            {!isLogin && (
                <div className="space-y-2">
                  <Label htmlFor="username">Username</Label>
                  <Input
                      id="username"
                      type="text"
                      placeholder="Your username"
                      value={username}
                      onChange={(e) => setUsername(e.target.value)}
                  />
                </div>
            )}

            <div className="space-y-2">
              <Label htmlFor="password">Password</Label>
              <Input
                  id="password"
                  type="password"
                  placeholder="••••••••"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </CardContent>

          <CardFooter className="flex flex-col gap-2">
            <Button
                className="w-full"
                onClick={() => signIn(email, username, password)}
            >
              {isLogin ? "Login" : "Sign Up"}
            </Button>

            {isLogin ? (
                <Button
                    variant="link"
                    className="w-full text-sm text-muted-foreground"
                    onClick={() => navigate(REGISTRATION_ROUTE)}
                >
                  Don't have an account?
                </Button>
            ) : (
                <Button
                    variant="link"
                    className="w-full text-sm text-muted-foreground"
                    onClick={() => navigate(LOGIN_ROUTE)}
                >
                  Already have an account?
                </Button>
            )}
          </CardFooter>
        </Card>
      </div>
  );
}

export default observer(Auth);
