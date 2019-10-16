import React from 'react';
import {slide as Menu} from 'react-burger-menu';
import './Menu.css';

const menu = () => {
    return (
        <Menu>
                <a className="bm-test" href="www.google.com">
                    <span className="bm-test">Home</span>
                </a>
                <a className="bm-test" href="www.google.com">
                    <span className="bm-test">MEN</span>
                </a>
                <a className="bm-test" href="www.google.com">
                    <span className="bm-test">WOMEN</span>
                </a>
                <a className="bm-test" href="www.google.com">
                    <span className="bm-test">BRANDS</span>
                </a>
                <a className="bm-test" href="www.google.com">
                    <span className="bm-test">STORY</span>
                </a>
                <a className="bm-test" href="www.google.com">
                    <span className="bm-test">LIFESTYLE</span>
                </a>
                <a className="bm-test" href="www.google.com">
                    <span className="bm-test">CONTACT</span>
                </a>
        </Menu>
    );
}

export default menu;
