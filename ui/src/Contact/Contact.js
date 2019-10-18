import React from 'react';
import './Contact.css';
import fb from './icone-facebook.svg';
import ig from './icone-instagram.svg';
import pr from './icone-pinterest.svg';

const contact = () => {
    return (
        <div>
        <div className="Container-div">
            <div>
                <a href="#" className="Contact-Link">
                    <span>Contact_US</span>
                </a>
                <a href="#" className="Contact-Link">
                    <span>Shipping_Delivery_Returns</span>
                </a>
                <a href="#" className="Contact-Link">
                    <span>Customer_Service</span>
                </a>
            </div>

            <div className="Icon-div">
                <img className="Icon" src={fb}/>
                <img className="Icon" src={ig}/>
                <img className="Icon" src={pr}/>
            </div>

            <div>
                <a href="#" className="Contact-Link">
                    <span>Green_Brand</span>
                </a>
                <a href="#" className="Contact-Link">
                    <span>Newsletter</span>
                </a>
                <a href="#" className="Contact-Link">
                    <span>FAQ</span>
                </a>
            </div>
        </div>
            <p className="Reserved-right">© 2019 got_it. All rights reserved</p>
        </div>
    );
}

export default contact;