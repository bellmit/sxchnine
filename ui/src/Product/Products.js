import React, {Component} from "react";
/*import {SimpleGrid, Box, ThemeProvider, CSSReset, Image, Badge} from "@chakra-ui/core";
import { Grid, Paper, Typography } from "@material-ui/core";*/
import {makeStyles} from '@material-ui/core/styles';
import {Grid, Image} from 'semantic-ui-react'
import Product from './Product';
import Gallery from "../Gallery/Gallery";
import p1 from "./p1.png";
import nikelogo from "./Logo_NIKE.svg";
import './Product.css';
import BannerMen from '../Banner/Banner';
import {Input, Icon, Segment} from 'semantic-ui-react';
import panier from '../Banner/image-panier.svg';
import Contact from '../Contact/Contact';


class Products extends Component {
    state = {
        products: [
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },
            {
                id: 1, name: 'Classic retro - ', brand: 'Nike', logo: '', images: [
                    {id: 1, name: 'Unknown1', url: 'Unknown1.png'},
                    {id: 2, name: 'Unknown2', url: 'Unknown2.png'},
                    {id: 3, name: 'Unknown3', url: 'Unknown3.png'}
                ]
            },

        ],
        change: ''
    }

    changeHandler = (event) => {
        this.setState({
            change: event.target.value
        })
    }

    render() {

        return (
            <div>
                <div>
                    <header>
                        <BannerMen/>
                    </header>
                </div>

                <div className="Product-Message">
                    Do you wanna got some?
                </div>
                <div className="Product-Search-Input">
                    <Segment inverted size='mini'>

                        <Input inverted icon={<Icon name='search' inverted circular link/>}
                               placeholder='Search...'
                               onChange={this.changeHandler}/>
                    </Segment>
                </div>
                <div>
                    <img src={panier} className="Banner-panier-img"/>
                </div>
                <div>{this.state.change}</div>

                <div className="Product-Container">
                    <Grid centered columns={3}>
                        <Grid.Row centered>

                            {this.state.products.map((product, index) => (
                                <Grid.Column mobile={16} tablet={8} computer={5} centered>
                                    <Product key={index} name={product.name}/>
                                    <br/>
                                </Grid.Column>

                            ))}
                        </Grid.Row>
                    </Grid>
                    <div className="Product-Empty-Div" />


                    <div className="Product-footer">
                        <Contact/>
                    </div>
                </div>

            </div>
        );
    }
}

export default Products;