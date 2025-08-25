package com.byllameister.modelstore.admin.sellers;

import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.sellers.*;
import com.byllameister.modelstore.users.UserNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/admin/sellers")
@AllArgsConstructor
@Tag(name = "Admin Seller", description = "Admin methods related to sellers")
public class AdminSellerController {
    private SellerService sellerService;

    @GetMapping
    public ResponseEntity<Page<SellerWithStatsResponse>> getAllSellers(Pageable pageable) {
        var sellers = sellerService.getAllSellers(pageable);
        return ResponseEntity.ok(sellers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerWithStatsResponse> getSeller(@PathVariable Long id) {
        var seller = sellerService.getSeller(id);
        return ResponseEntity.ok(seller);
    }

    @PostMapping
    public ResponseEntity<SellerResponse> createSeller(
            @Valid @RequestBody CreateSellerRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var seller = sellerService.createSeller(request);
        var uri = uriComponentsBuilder.path("/sellers/{productId}").buildAndExpand(seller.getId()).toUri();
        return ResponseEntity.created(uri).body(seller);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SellerResponse> updateSeller(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSellerRequest request) {
        var seller = sellerService.updateSeller(id, request);
        return ResponseEntity.ok(seller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SellerNotFoundException.class)
    public ResponseEntity<ErrorDto> handleSellerNotFoundException(SellerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(SellerAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleSellerAlreadyExistsException(SellerAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }
}
