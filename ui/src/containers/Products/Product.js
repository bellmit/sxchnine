import React, {Component} from 'react';
import {Badge, CSSReset, ThemeProvider} from "@chakra-ui/core";
import './Product.css';
import {Card, Image} from "semantic-ui-react";

class Product extends Component {

    shouldComponentUpdate(nextProps, nextState) {
        return this.props.name !== nextProps.name;
    }
    render() {

        return (
            <ThemeProvider>
                <CSSReset/>
            <Card className="Product-Card-Size">
                <Image src={this.props.image[0]}
                       className="Product-Image-Size"
                       size="large"
                       onClick={this.props.clicked}/>
                <Card.Content>
                    <Badge rounded="full" px="2" variantColor="teal" style={{margin: '5% 0 5% 0'}}>
                        New
                    </Badge>
                    <Card.Header>
                        <span className="Product-Text">{this.props.brand}</span>
                    </Card.Header>
                    <Card.Description>
                        <span className="Product-Text">{this.props.name}</span>
                    </Card.Description>
                    <Card.Description>
                        <span className="Product-Text">{this.props.price} $</span>
                    </Card.Description>
                    <Card.Meta>
                        <span className="Product-Text-Size">{this.props.size.join(' - ')}</span>
                    </Card.Meta>
                </Card.Content>
            </Card>
            </ThemeProvider>
        );
    }
}

export default Product;