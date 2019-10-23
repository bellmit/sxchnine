import React from 'react';
import {ThemeProvider, CSSReset, Box, Image, Badge} from "@chakra-ui/core";
import p1 from './p1.png';
import nikelogo from './Logo_NIKE.svg';


const product = (props) => {
    return (
        <ThemeProvider>
            <CSSReset/>
            <Box maxW="80%" borderWidth="1px" rounded="lg" overflow="hidden">
                <Image src={p1} alt={props.name} maxW="70%"/>
                <Box p="6">
                    <Box d="flex" alignItems="baseline">
                        <Badge rounded="full" px="2" variantColor="teal">
                            New
                        </Badge>
                    </Box>
                    <Box maxW="30%">
                        <Image src={nikelogo} alt={props.name}/>
                    </Box>

                    <Box
                        mt="1"
                        fontFamily="Anton"
                        as="h4"
                        lineHeight="tight"
                        isTruncated>
                        NIKE - Classic Hoodie -
                    </Box>

                    <Box fontFamily="Anton">
                        $80
                    </Box>
                    <Box as="span" color="gray.600" fontSize="sm" fontFamily="Anton">
                        Black - M
                    </Box>
                </Box>

            </Box>
        </ThemeProvider>
    );
}

export default product;