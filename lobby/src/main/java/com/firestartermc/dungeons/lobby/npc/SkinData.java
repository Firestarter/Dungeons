package com.firestartermc.dungeons.lobby.npc;

import java.util.Objects;

public class SkinData {

    public static final SkinData DEFAULT = new SkinData(
            "ewogICJ0aW1lc3RhbXAiIDogMTU5NjEzMzEwNTIwOSwKICAicHJvZmlsZUlkIiA6ICI5M2Y2NGY2OWRhODI0ZmM3YTFhZDdiYjQ0MmYxNzMyYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ0b3RhbGx5X2FkbWluIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzQ2ZjI5MjQxYjQ4MTM4YzkzYzE1MWYyMGY4YmM2NzhlZDhkNDQxN2RhMTNlNGZkNTM0NjhhNjI4ZjZhYzllYTgiCiAgICB9CiAgfQp9",
            "WNDCaw/2MDeF9VacQn7AirvpzsKotInFgxxUNMoBL2z4CnuXRC330fYL3pTf2rXgadhu4Y2BGDQC1PwnaItr770hasyFH5olu2MZiecSA6AYanjmyd7rNJcpnGDl80cwyeS9UVkLqvy3FDTzsy8/LfkJwIJ4tiPN8vg6F1dZY78JnSZhWQQIPlXwcbc2Lg3b06yh9eF1wetrfVTcgub6pkKWLfmo6ClXyaxwU6N6mofrl+cC8MRliYRQjcJqg4qne4VqFrxLNh2BQSopNpswky/v/5ZrMDJI5YjYK4MWAe1UT+BVTi60V6u18l65f5i4HbtfNik0fKD88yBbosg0IPTmLiCE3zh46FFdBP7IVYfWU2qQJ4qn78kVF+z6mDqsy4BlZPbwrLuKMQPEdl94qElNDrweCJTxTa4Vtcq/ikB/o6+25XfSGVnIjjIuuj+YTL7RgeNdhe2hZVN1NGLQZadnQD3pSc/ix9Hi1eT/ujop0YHh0+BkkSQMpMDBRVsK0rMy2/IdHdLtDW64qXbJEvD6FXxflCeLaKotfXtvbatALKyMHKh1MEi5gRSpDlKWlBWyE7IDWcomp0LQYRnfla85ev7rEpKlc9JcCK0W73GBup5/mY5JubSGP+QI+B2JhlmhT8S1rsf95jMjpH00Y2Qs2I4BifFKRgpCAfLhsJk="
    );

    private final String texture;
    private final String signature;

    public SkinData(String texture, String signature) {
        this.texture = texture;
        this.signature = signature;
    }

    public String getTexture() {
        return texture;
    }

    public String getSignature() {
        return signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkinData skinData = (SkinData) o;
        return Objects.equals(texture, skinData.texture) &&
                Objects.equals(signature, skinData.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texture, signature);
    }
}
