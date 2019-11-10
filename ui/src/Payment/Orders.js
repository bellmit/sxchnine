import React, {Component} from 'react';
import './Orders.css';
import OrderPlaceBanner from "../Banner/OrderPlaceBanner";
import Card from './Card';
import Contact from '../Contact/Contact';
import {Form, Grid, Image, Label, Input} from "semantic-ui-react";

class Orders extends Component {

    state = {
        products: [
            {
                id: 1, name: 'Classic retro - 90s', brand: 'Nike', logo: '', images: [
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
        open: false
    }

    render() {


        return (
            <div>
                <div className="Orders-Yellow-bar-div" />
                <header>
                    <OrderPlaceBanner />
                </header>
                <div className="Orders-Bag-Resume">
                <span className="Orders-Resume-Text">Your Bag : </span>
                </div>
                <div className="Orders-Resume">
                    <Grid columns={2} centered>
                        {this.state.products.map((product, index) => (
                            <Grid.Row centered>
                                <Grid.Column width={3}>
                                    <Image src={product.images[0].url} size='small' circular />
                                </Grid.Column>
                                <Grid.Column width={3}>
                                    <span className="Orders-Items-Text-Header">{product.name}</span>
                                    <p className="Orders-Items-Text">Black </p>
                                    <p className="Orders-Items-Text">Small</p>
                                    <p className="Orders-Items-Text">$90</p>
                                </Grid.Column>
                            </Grid.Row>
                        ))}
                        <Grid.Row>
                            <Grid.Column width={3} height={4}>
                                <p className="Orders-Total-Text">TOTAL:</p>
                            </Grid.Column>
                            <Grid.Column width={3}>
                                <Label tag color='red'>$200</Label>
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <span className="Orders-Yellow-second-bar-div"></span>
                        </Grid.Row>
                    </Grid>
                    <Grid centered>
                        <Grid.Row>
                            <Grid.Column width={3}>
                                <p className="Orders-Email-Text">EMAIL ADDRESS:</p>
                            </Grid.Column>

                            <Grid.Column width={3}>
                                <Input  inverted placeholder='email address...'
                                />
                            </Grid.Column>
                        </Grid.Row>

                        <Grid.Row>
                            <Grid.Column width={3}>
                                <p className="Orders-Email-Text">DELIVERY ADDRESS:</p>
                            </Grid.Column>

                            <Grid.Column width={3}>
                                <Input  inverted placeholder='delivery address...'/>
                            </Grid.Column>
                        </Grid.Row>
                        <Grid.Row>
                            <span className="Orders-Yellow-second-bar-div"></span>
                        </Grid.Row>
                    </Grid>
                    <Card />
                    <Contact />

                </div>
            </div>
        );
    }
}

export default Orders;