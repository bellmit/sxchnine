import React from 'react';
import shipping_plan from './shipping_back.png';
import './Shipping.css';

const shipping = () => {
    return (
        <div>
            <img src={shipping_plan} alt="shipping" className="shipping-img" />
        </div>
    )
}

export default shipping;