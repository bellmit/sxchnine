import React from 'react';
import {slide as Menu} from 'react-burger-menu';
import {NavLink} from "react-router-dom";
import './Menu.css';

const menu = () => {
    return (
        <Menu>
            <NavLink to="/"  exact className="bm-test">
                <span className="bm-test">Home</span>
            </NavLink>
            <NavLink to="/men" exact className="bm-test" >
                <span className="bm-test">MEN</span>
            </NavLink>
            <NavLink to="/women" exact className="bm-test">
                <span className="bm-test">WOMEN</span>
            </NavLink>
            <NavLink to="/brands" exact className="bm-test">
                <span className="bm-test">BRANDS</span>
            </NavLink>
            <NavLink to="/story" exact className="bm-test">
                <span className="bm-test">STORY</span>
            </NavLink>
            <NavLink to="/lifestyle" exact className="bm-test">
                <span className="bm-test">LIFESTYLE</span>
            </NavLink>
            <NavLink to="/contact" exact className="bm-test">
                <span className="bm-test">CONTACT</span>
            </NavLink>
        </Menu>
    );
}

export default menu;
