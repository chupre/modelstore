import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Button } from "@/components/ui/button";
import { useLocation, useNavigate } from "react-router-dom";
import { LOGIN_ROUTE, REGISTRATION_ROUTE } from '../utils/consts';

export default function Auth() {
  const location = useLocation();
  const isLogin = location.pathname === LOGIN_ROUTE;
  const navigate = useNavigate();

  return (
    <div className="w-full min-h-screen flex items-center justify-center bg-background px-4 pt-20">
      <Card className="w-full max-w-md p-4 -mt-30">
        <CardHeader>
          <CardTitle className="text-2xl text-center">Sign {isLogin ? "In" : "Up"}</CardTitle>
        </CardHeader>

        <CardContent className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="email">Email</Label>
            <Input id="email" type="email" placeholder="you@example.com" />
          </div>

          {!isLogin && (
            <div className="space-y-2">
              <Label htmlFor="username">Username</Label>
              <Input id="username" type="text" placeholder="Your username" />
            </div>
          )}

          <div className="space-y-2">
            <Label htmlFor="password">Password</Label>
            <Input id="password" type="password" placeholder="••••••••" />
          </div>
        </CardContent>

        <CardFooter className="flex flex-col gap-2">
          <Button className="w-full">{isLogin ? "Login" : "Sign Up"}</Button>
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