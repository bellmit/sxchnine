import React, {Component} from 'react';
import {slide as Menu} from 'react-burger-menu';
import {NavLink} from "react-router-dom";
import './Menu.css';

class MenuBurger extends Component {

    state = {
        menuOpen: false
    }

    handleStateChange (state) {
        this.setState({menuOpen: state.isOpen})
    }

    closeMenu = () => {
        this.setState(
            {menuOpen: false}
        )
    }

    render() {
        return (

            <Menu isOpen={this.state.menuOpen} onStateChange={(state) => this.handleStateChange(state)}>
                <NavLink to="/" exact className="bm-test">
                    <span onClick={this.closeMenu}>Home</span>
                </NavLink>
                <NavLink to="/men" exact className="bm-test">
                    <span onClick={this.closeMenu}>MEN</span>
                </NavLink>
                <NavLink to="/women" exact className="bm-test">
                    <span onClick={this.closeMenu}>WOMEN</span>
                </NavLink>
{/*                <NavLink to="/brands" exact className="bm-test">
                    <span onClick={this.closeMenu}>BRANDS</span>
                </NavLink>*/}
                <NavLink to="/ourStory" exact className="bm-test">
                    <span onClick={this.closeMenu}>STORY</span>
                </NavLink>
                <NavLink to="/subscription" exact className="bm-test">
                    <span onClick={this.closeMenu}>SUBSCRIPTION</span>
                </NavLink>
                <NavLink to="/contactUs" exact className="bm-test">
                    <span onClick={this.closeMenu}>CONTACT</span>
                </NavLink>
            </Menu>
        );
    }
}

export default MenuBurger;
