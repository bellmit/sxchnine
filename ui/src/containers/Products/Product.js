import React, {Component} from 'react';
import {ThemeProvider, CSSReset, Box, Badge} from "@chakra-ui/core";
import { Image } from 'semantic-ui-react';
import './Product.css';

class Product extends Component {

    shouldComponentUpdate(nextProps, nextState) {
        console.log('Product.js should component update');
        console.log(this.props.name !== nextProps.name);
        return this.props.name !== nextProps.name;
    }

    componentDidUpdate(prevProps: Readonly<P>, prevState: Readonly<S>, snapshot: SS): void {
        console.log('Product.js did update');
        console.log(this.props);
    }

    render(){

        return (
            <ThemeProvider>
                <CSSReset/>
                <Box w={this.props.width} h={this.props.height} borderWidth="1px" rounded="lg" overflow="hidden" maxH="90%">
                    <Box h="60%" textAlign="center">
                    <img src={this.props.image[0]} alt={this.props.name}
                         onClick={this.props.clicked}
                         style={{cursor: 'pointer'}}/>
                    </Box>
                    <Box p="6">
                        <Box d="flex" alignItems="baseline">
                            <Badge rounded="full" px="2" variantColor="teal" style={{margin: '5% 0 5% 0'}}>
                                New
                            </Badge>
                        </Box>
                        <Box maxW="30%">
                            <Image src={this.props.logo} alt={this.props.name}/>
                        </Box>

                        <Box
                            mt="1"
                            fontFamily="Anton"
                            as="h4"
                            lineHeight="tight"
                            isTruncated>
                            {this.props.brand} - {this.props.name}
                        </Box>

                        <Box fontFamily="Anton">
                            ${this.props.price}
                        </Box>
                        <Box as="span" color="gray.600" fontSize="sm" fontFamily="Anton">
                            {this.props.size.join(' - ')}
                        </Box>
                    </Box>

                </Box>
            </ThemeProvider>
        );
    }
}

export default Product;