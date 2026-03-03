package com.cboard.owlswap.owlswap_backend.security;

import com.cboard.owlswap.owlswap_backend.model.Item;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class ItemAuthorizer
{
    private final CurrentUser currentUser;
    public ItemAuthorizer(CurrentUser currentUser)
    {
        this.currentUser = currentUser;
    }

    // Throws AccessDeniedException (-> 403) if the current user does not own the item.
    public void requireOwner(Item item) {
        Integer currentUserId = currentUser.userId();
        Integer ownerId = (item.getUser() == null) ? null : item.getUser().getUserId();

        if (currentUserId == null) {
            throw new AccessDeniedException("Not authenticated.");
        }

        if (ownerId == null || !ownerId.equals(currentUserId)) {
            throw new AccessDeniedException("You do not own this item.");
        }
    }




}
