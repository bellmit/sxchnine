import React, {Component} from 'react';
import {Badge, Box, CSSReset, ThemeProvider} from "@chakra-ui/core";
import './Product.css';
import {Card, Grid, Image} from "semantic-ui-react";

class Product extends Component {

    shouldComponentUpdate(nextProps, nextState) {
        return this.props.name !== nextProps.name;
    }

    componentDidUpdate(prevProps: Readonly<P>, prevState: Readonly<S>, snapshot: SS): void {
        console.log('Product.js did update');
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
/*            <ThemeProvider>
                <CSSReset/>
                <Box w={this.props.width} h={this.props.height} className="Product-div"
                     borderWidth="1px"
                     rounded="lg"
                     overflow="hidden"
                     maxH="90%">
                    <Box maxH="50%" minH="50%" textAlign="center">
                        <img src={this.props.image[0]} alt={this.props.name}
                             onClick={this.props.clicked}
                             style={{cursor: 'pointer'}}/>
                    </Box>
                    <Box>
                        <Box p="6">
                            <Box d="flex" alignItems="baseline">
                                <Badge rounded="full" px="2" variantColor="teal" style={{margin: '5% 0 5% 0'}}>
                                    New
                                </Badge>
                            </Box>
                            {/!*                        <Box maxW="30%" minW="30%" marginTop="30%">
                            <Image src={this.props.logo} alt={this.props.name}/>
                        </Box>*!/}

                            <Box
                                mt="1"
                                fontFamily="Anton"
                                as="h4"
                                lineHeight="tight"
                                isTruncated>
                                {this.props.brand}
                            </Box>
                            <Box
                                mt="1"
                                fontFamily="Anton"
                                as="h4"
                                lineHeight="tight"
                                isTruncated>
                                {this.props.name}
                            </Box>

                            <Box fontFamily="Anton">
                                ${this.props.price}
                            </Box>
                            <Box as="span" color="gray.600" fontSize="sm" fontFamily="Anton">
                                {this.props.size.join(' - ')}
                            </Box>
                        </Box>
                    </Box>

                </Box>
            </ThemeProvider>*/
        );
    }
}

export default Product;