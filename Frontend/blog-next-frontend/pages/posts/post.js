import { Stack, Typography, Button, Avatar, Box } from "@mui/material";
import { Image } from "mui-image"; //https://github.com/benmneb/mui-image
import ResponsiveAppBar from "../../resource/components/global/headerBar";
import posts from "../../data/mock_db/posts";

export default function Post() {
  const post = posts[0];

  return (
    <>
      <ResponsiveAppBar />
      <Box sx={{ width: "60%", mx: "auto", mt: 5 }}>
        <Stack direction="column" spacing={2} sx={{ mb: 2 }}>
          <Box component="img" src={post.image.href} />
          <Typography variant="h1" component="h2">
            {post.title}
          </Typography>
          <Stack direction="row" spacing={2} sx={{ mb: 2 }}>
            <Avatar src={post.user.image.href} alt={post.user.username} />
            <Typography variant="h4" component="h3">
              {post.user.username}
            </Typography>
          </Stack>
          <Typography variant="body1" component="p">
            {post.content}
          </Typography>
        </Stack>
      </Box>
    </>
  );
}
