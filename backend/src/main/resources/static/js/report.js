async function validatePost(validateButton) {
  let postId = validateButton.getAttribute("data-id");

  const response = await fetch("/p/validate/" + postId, {
    method: "GET",
  });

  if (!response.ok) {
    throw new Error("Network response was not ok");
  }
}
