package com.manager.hamster.model.EventBus;

import com.manager.hamster.model.sanPhamMoi;

public class SuaXoaEvent {
    sanPhamMoi  sanPhamMoi;

    public SuaXoaEvent(com.manager.hamster.model.sanPhamMoi sanPhamMoi) {
        this.sanPhamMoi = sanPhamMoi;
    }

    public com.manager.hamster.model.sanPhamMoi getSanPhamMoi() {
        return sanPhamMoi;
    }

    public void setSanPhamMoi(com.manager.hamster.model.sanPhamMoi sanPhamMoi) {
        this.sanPhamMoi = sanPhamMoi;
    }
}
