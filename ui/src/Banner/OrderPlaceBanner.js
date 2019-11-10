import React from "react";
import orderPlace from './order_place.jpg';
import './OrderPlaceBanner.css';

const OrderPlaceBanner = () => {
    return (
        <div>
            <img src={orderPlace} className="BannerOrder-Header-img"/>
            <div className="BannerOrder-Container-Men">
                <p>ORDER PLACE </p>
            </div>
            <div className="BannerOrder-Empty-Div" />
        </div>
    );
}

export default OrderPlaceBanner;